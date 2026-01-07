package com.example.check.controller;

import com.example.check.pojo.ItemOverview;
import com.example.check.service.ItemOverviewService;
import com.example.check.util.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物品总览控制器
 */
@RestController
@RequestMapping("/api/item-overview")
@CrossOrigin(origins = "*")
public class ItemOverviewController {
    
    @Autowired
    private ItemOverviewService itemOverviewService;
    
    /**
     * 获取所有物品总览
     */
    @GetMapping
    public Map<String, Object> getAll() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ItemOverview> items = itemOverviewService.getAll();
            result.put("success", true);
            result.put("data", items);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 管理员用获取所有物品总览
     */
    @GetMapping("/Admin")
    public Map<String, Object> AdminGetAll() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ItemOverview> items = itemOverviewService.AdminGetAll();
            result.put("success", true);
            result.put("data", items);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    /**
     * 根据ID查询物品总览
     */
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            ItemOverview item = itemOverviewService.getById(id);
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
     * 根据分类ID查询物品总览
     */
    @GetMapping("/category/{categoryId}")
    public Map<String, Object> getByCategoryId(@PathVariable Integer categoryId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ItemOverview> items = itemOverviewService.getByCategoryId(categoryId);
            result.put("success", true);
            result.put("data", items);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据名称搜索物品总览
     */
    @GetMapping("/search")
    public Map<String, Object> getByName(@RequestParam String name) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ItemOverview> items = itemOverviewService.getByName(name);
            result.put("success", true);
            result.put("data", items);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "搜索失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 添加物品总览（支持用户自定义物品）
     */
    @PostMapping
    public Map<String, Object> add(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();
        try {
            ItemOverview itemOverview = new ItemOverview();
            String name = (String) requestData.get("name");
            String description = requestData.get("description") != null ? (String) requestData.get("description") : null;
            // 敏感词检测
            if (name != null && SensitiveWordFilter.containsSensitiveWord(name)) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (description != null && SensitiveWordFilter.containsSensitiveWord(description)) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            itemOverview.setName(name);
            itemOverview.setCategoryId((Integer) requestData.get("categoryId"));
            itemOverview.setDescription(description);
            itemOverview.setImageUrl(requestData.get("imageUrl") != null ? (String) requestData.get("imageUrl") : null);
            itemOverview.setIsActive(requestData.get("isActive") != null ? (Boolean) requestData.get("isActive") : true);
            // 处理tags字段：支持List<String>格式
            if (requestData.get("tags") != null) {
                Object tagsObj = requestData.get("tags");
                System.out.println(tagsObj);
                if (tagsObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<String> tags = (List<String>) tagsObj;
                    System.out.println(tagsObj);
                    itemOverview.setTags(tags);
                }
            }
            
            boolean success = itemOverviewService.add(itemOverview);
            if (success) {
                result.put("success", true);
                result.put("message", "添加成功");
                result.put("data", itemOverview);
            } else {
                result.put("success", false);
                result.put("message", "添加失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 更新物品总览
     */
    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Integer id, @RequestBody ItemOverview itemOverview) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 敏感词检测
            if (itemOverview.getName() != null && SensitiveWordFilter.containsSensitiveWord(itemOverview.getName())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (itemOverview.getDescription() != null && SensitiveWordFilter.containsSensitiveWord(itemOverview.getDescription())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            itemOverview.setId(id);
            boolean success = itemOverviewService.update(itemOverview);
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
     * 删除物品总览
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = itemOverviewService.deleteById(id);
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
     * 统计物品总览数量
     */
    @GetMapping("/count")
    public Map<String, Object> count() {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = itemOverviewService.count();
            result.put("success", true);
            result.put("data", count);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据分类统计物品总览数量
     */
    @GetMapping("/count/category/{categoryId}")
    public Map<String, Object> countByCategoryId(@PathVariable Integer categoryId) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = itemOverviewService.countByCategoryId(categoryId);
            result.put("success", true);
            result.put("data", count);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据条件筛选物品总览（支持分类、关键词筛选）
     */
    @GetMapping("/filter")
    public Map<String, Object> getFilteredItems(
            @RequestParam(required = false) String categoryCode,
            @RequestParam(required = false) String keyword) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ItemOverview> items = itemOverviewService.getFilteredItems(categoryCode, keyword);
            result.put("success", true);
            result.put("data", items);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "筛选失败：" + e.getMessage());
        }
        return result;
    }
}

