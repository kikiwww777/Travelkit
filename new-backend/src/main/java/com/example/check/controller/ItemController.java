package com.example.check.controller;

import com.example.check.pojo.Item;
import com.example.check.pojo.Trip;
import com.example.check.service.ItemService;
import com.example.check.service.ItemOverviewService;
import com.example.check.service.TripService;
import com.example.check.util.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物品控制器
 */
@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private ItemOverviewService itemOverviewService;
    
    @Autowired
    private TripService tripService;
    
    /**
     * 根据ID查询物品
     */
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Item item = itemService.getById(id);
            if (item != null) {
                result.put("success", true);
                result.put("data", item);
            } else {
                result.put("success", false);
                result.put("message", "物品不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据行程ID查询物品列表
     */
    @GetMapping("/trip/{tripId}")
    public Map<String, Object> getByTripId(@PathVariable Integer tripId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Item> items = itemService.getByTripId(tripId);
            result.put("success", true);
            result.put("data", items);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 添加物品（必须关联item_overview）
     */
    @PostMapping
    public Map<String, Object> add(@RequestBody Item item) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (item.getTripId() == null) {
                result.put("success", false);
                result.put("message", "缺少行程信息，无法添加物品");
                return result;
            }
            Trip trip = tripService.getById(item.getTripId());
            if (trip == null) {
                result.put("success", false);
                result.put("message", "行程不存在，无法添加物品");
                return result;
            }
            String status = trip.getStatus() != null ? trip.getStatus().toLowerCase() : "";
            if ("completed".equals(status) || "cancelled".equals(status)) {
                result.put("success", false);
                result.put("message", "行程已完成，无法再添加物品");
                return result;
            }
            if (item.getItemOverviewId() == null) {
                result.put("success", false);
                result.put("message", "物品必须关联item_overview");
                return result;
            }
            // 敏感词检测
            if (item.getName() != null && SensitiveWordFilter.containsSensitiveWord(item.getName())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (item.getNote() != null && SensitiveWordFilter.containsSensitiveWord(item.getNote())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            boolean success = itemService.add(item);
            if (success) {
                result.put("success", true);
                result.put("message", "添加成功");
                result.put("data", item);
            } else {
                result.put("success", false);
                result.put("message", "添加失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 批量添加物品
     */
    @PostMapping("/batch")
    public Map<String, Object> batchAdd(@RequestBody List<Item> items) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = itemService.batchAdd(items);
            if (success) {
                result.put("success", true);
                result.put("message", "批量添加成功");
            } else {
                result.put("success", false);
                result.put("message", "批量添加失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "批量添加失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新物品
     */
    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Integer id, @RequestBody Item item) {
        Map<String, Object> result = new HashMap<>();
        try {
            item.setId(id);
            boolean success = itemService.update(item);
            if (success) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除物品
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = itemService.deleteById(id);
            if (success) {
                result.put("success", true);
                result.put("message", "删除成功");
            } else {
                result.put("success", false);
                result.put("message", "删除失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新物品查验状态
     * checked: 0=未查验, 1=已查验, 2=已跳过
     */
    @PutMapping("/{id}/check")
    public Map<String, Object> updateCheckedStatus(@PathVariable Integer id, @RequestParam Integer checked) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (checked == null || (checked != 0 && checked != 1 && checked != 2)) {
                result.put("success", false);
                result.put("message", "checked参数必须为0、1或2");
                return result;
            }
            boolean success = itemService.updateCheckedStatus(id, checked);
            if (success) {
                result.put("success", true);
                result.put("message", "状态更新成功");
            } else {
                result.put("success", false);
                result.put("message", "状态更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "状态更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据分类查询物品
     */
    @GetMapping("/trip/{tripId}/category/{categoryId}")
    public Map<String, Object> getByCategory(@PathVariable Integer tripId, @PathVariable Integer categoryId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Item> items = itemService.getByCategory(tripId, categoryId);
            result.put("success", true);
            result.put("data", items);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据查验状态查询物品
     * checked: 0=未查验, 1=已查验, 2=已跳过
     */
    @GetMapping("/trip/{tripId}/checked/{checked}")
    public Map<String, Object> getByCheckedStatus(@PathVariable Integer tripId, @PathVariable Integer checked) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Item> items = itemService.getByCheckedStatus(tripId, checked);
            result.put("success", true);
            result.put("data", items);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计行程物品总数
     */
    @GetMapping("/trip/{tripId}/count")
    public Map<String, Object> countByTripId(@PathVariable Integer tripId) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = itemService.countByTripId(tripId);
            result.put("success", true);
            result.put("data", count);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 统计已查验物品数
     */
    @GetMapping("/trip/{tripId}/checked-count")
    public Map<String, Object> countCheckedByTripId(@PathVariable Integer tripId) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = itemService.countCheckedByTripId(tripId);
            result.put("success", true);
            result.put("data", count);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    
    /**
     * 根据物品总览ID删除旅程物品
     */
    @DeleteMapping("/trip/{tripId}/item-overview/{itemOverviewId}")
    public Map<String, Object> deleteByItemOverviewId(@PathVariable Integer tripId, @PathVariable Integer itemOverviewId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = itemService.deleteByItemOverviewId(tripId, itemOverviewId);
            if (success) {
                result.put("success", true);
                result.put("message", "删除成功");
            } else {
                result.put("success", false);
                result.put("message", "删除失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据物品总览ID查询旅程物品
     */
    @GetMapping("/trip/{tripId}/item-overview/{itemOverviewId}")
    public Map<String, Object> getByItemOverviewId(@PathVariable Integer tripId, @PathVariable Integer itemOverviewId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Item item = itemService.getByItemOverviewId(tripId, itemOverviewId);
            if (item != null) {
                result.put("success", true);
                result.put("data", item);
            } else {
                result.put("success", false);
                result.put("message", "物品不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取物品总览列表（包括公共物品和指定用户的自定义物品）
     */
    @GetMapping("/overview")
    public Map<String, Object> getItemOverview() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<com.example.check.pojo.ItemOverview> items = itemOverviewService.getAll();
            result.put("success", true);
            result.put("data", items);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据条件筛选物品（支持分类、状态、关键词筛选）
     * checked: 0=未查验, 1=已查验, 2=已跳过
     */
    @GetMapping("/trip/{tripId}/filter")
    public Map<String, Object> getFilteredItems(
            @PathVariable Integer tripId,
            @RequestParam(required = false) String categoryCode,
            @RequestParam(required = false) Integer checked,
            @RequestParam(required = false) String keyword) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Item> items = itemService.getFilteredItems(tripId, categoryCode, checked, keyword);
            result.put("success", true);
            result.put("data", items);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "筛选失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除查验记录（取消勾选物品并删除查验历史）
     * 将checked设置为0（未查验），并删除checked_at时间
     */
    @DeleteMapping("/trip/{tripId}/item/{itemId}/check-record")
    public Map<String, Object> removeCheckRecord(@PathVariable Integer tripId, @PathVariable Integer itemId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1. 更新物品的checked状态为0（未查验），并清空checked_at
            Item item = itemService.getById(itemId);
            if (item == null) {
                result.put("success", false);
                result.put("message", "物品不存在");
                return result;
            }
            item.setChecked(0);
            item.setCheckedAt(null);
            boolean updateSuccess = itemService.update(item);
            if (!updateSuccess) {
                result.put("success", false);
                result.put("message", "更新物品状态失败");
                return result;
            }
            result.put("success", true);
            result.put("message", "删除查验记录成功");
        }
        catch (Exception e){
            result.put("error", e);
        }
        return result;
    }
    
    /**
     * 重置所有勾选状态（批量删除行程中所有已勾选的物品）
     */
    @DeleteMapping("/trip/{tripId}/checked/reset-all")
    public Map<String, Object> resetAllCheckedItems(@PathVariable Integer tripId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = itemService.deleteAllCheckedItemsByTripId(tripId);
            if (success) {
                result.put("success", true);
                result.put("message", "重置成功，所有勾选状态已清空");
            } else {
                result.put("success", false);
                result.put("message", "重置失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "重置失败：" + e.getMessage());
        }
        return result;
    }
}