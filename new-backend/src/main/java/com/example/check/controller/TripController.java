package com.example.check.controller;

import com.example.check.pojo.Trip;
import com.example.check.pojo.TripTemplate;
import com.example.check.service.TripService;
import com.example.check.service.ItemService;
import com.example.check.service.TripTemplateService;
import com.example.check.util.SensitiveWordFilter;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行程控制器
 */
@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "*")
public class TripController {
    
    @Autowired
    private TripService tripService;
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private TripTemplateService tripTemplateService;
    
    /**
     * 根据ID查询行程
     */
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Trip trip = tripService.getById(id);
            if (trip != null) {
                // 从items表中查询并计算查验进度
                int totalItems = itemService.countByTripId(id);
                int checkedItems = itemService.countCheckedByTripId(id);
                int progress = totalItems > 0 ? (int) Math.round((double) checkedItems / totalItems * 100) : 0;
                
                // 更新trip对象的统计数据
                trip.setTotalItems(totalItems);
                trip.setCheckedItems(checkedItems);
                trip.setProgress(progress);
                
                // 同步更新数据库中的进度信息
                tripService.updateProgress(id, checkedItems, totalItems, progress);
                
                result.put("success", true);
                result.put("data", trip);
            } else {
                result.put("success", false);
                result.put("message", "行程不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据用户ID查询行程列表
     */
    @GetMapping("/user/{userId}")
    public Map<String, Object> getByUserId(@PathVariable Integer userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Trip> trips = tripService.getByUserId(userId);
            result.put("success", true);
            result.put("data", trips);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 创建行程
     */
    @PostMapping
    public Map<String, Object> create(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 提取行程数据
            Trip trip = new Trip();
            trip.setUserId((Integer) requestData.get("userId"));
            String destination = (String) requestData.get("destination");
            String rawName = (String) requestData.get("name");
            
            // 敏感词检测 - 目的地
            if (destination != null && SensitiveWordFilter.containsSensitiveWord(destination)) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            
            // 敏感词检测 - 名称
            if (rawName != null && !rawName.trim().isEmpty()) {
                if (SensitiveWordFilter.containsSensitiveWord(rawName)) {
                    result.put("success", false);
                    result.put("message", "请勿输入敏感内容");
                    return result;
                }
                trip.setName(rawName.trim());
            } else if (destination != null && !destination.trim().isEmpty()) {
                trip.setName(destination.trim());
            }
            
            trip.setDestination(destination);
            if (requestData.get("startDate") != null) {
                trip.setStartDate(java.time.LocalDate.parse((String) requestData.get("startDate")));
            }
            if (requestData.get("endDate") != null) {
                trip.setEndDate(java.time.LocalDate.parse((String) requestData.get("endDate")));
            }
            trip.setDuration((Integer) requestData.get("duration"));
            trip.setTravelers((Integer) requestData.get("travelers"));
            trip.setType((String) requestData.get("type"));
            trip.setBudget((String) requestData.get("budget"));
            String description = (String) requestData.get("description");
            String specialNeeds = (String) requestData.get("specialNeeds");
            if (description != null && SensitiveWordFilter.containsSensitiveWord(description)) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (specialNeeds != null && SensitiveWordFilter.containsSensitiveWord(specialNeeds)) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            trip.setDescription(description);
            trip.setSpecialNeeds(specialNeeds);
            trip.setStatus((String) requestData.get("status"));
            // 位置ID（和风城市ID，可能为字符串如 "AC16C"）
            Object locationId = requestData.get("locationId");
            if (locationId != null) {
                String locationIdStr = String.valueOf(locationId).trim();
                if (!locationIdStr.isEmpty() && !"null".equalsIgnoreCase(locationIdStr)) {
                    trip.setLocationId(locationIdStr);
                }
            }
            // 行程封面图片
            String img = (String) requestData.get("img");
            if (img != null && !img.trim().isEmpty()) {
                trip.setImg(img.trim());
            }
            
            // 提取模板ID（可选）
            Integer templateId = null;
            if (requestData.get("templateId") != null) {
                templateId = (Integer) requestData.get("templateId");
            }
            
            if (templateId != null) {
                TripTemplate template = tripTemplateService.getById(templateId);
                if (template == null) {
                    result.put("success", false);
                    result.put("message", "模板不存在或已删除");
                    return result;
                }
                if (!Boolean.TRUE.equals(template.getIsPublic())) {
                    if (trip.getUserId() == null || template.getCreatedBy() == null
                            || !template.getCreatedBy().equals(trip.getUserId())) {
                        result.put("success", false);
                        result.put("message", "该模板仅创建者可使用");
                        return result;
                    }
                }
            }
            
            // 创建行程
            boolean success = tripService.create(trip);
            if (success) {
                // 如果指定了模板ID，根据模板创建物品
                if (templateId != null && trip.getId() != null) {
                    try {
                        boolean itemsCreated = itemService.createFromTemplate(trip.getId(), templateId);
                        if (itemsCreated) {
                            // 更新行程的物品统计
                            int totalItems = itemService.countByTripId(trip.getId());
                            tripService.updateProgress(trip.getId(), 0, totalItems, 0);
                            result.put("message", "创建成功，已根据模板生成物品清单");
                        } else {
                            result.put("message", "创建成功，但模板物品导入失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        result.put("message", "创建成功，但模板物品导入失败：" + e.getMessage());
                    }
                } else {
                    result.put("message", "创建成功");
                }
                
                result.put("success", true);
                result.put("data", trip);
            } else {
                result.put("success", false);
                result.put("message", "创建失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "创建失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新行程
     */
    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Integer id, @RequestBody Trip trip) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 敏感词检测
            if (trip.getName() != null && SensitiveWordFilter.containsSensitiveWord(trip.getName())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (trip.getDescription() != null && SensitiveWordFilter.containsSensitiveWord(trip.getDescription())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (trip.getSpecialNeeds() != null && SensitiveWordFilter.containsSensitiveWord(trip.getSpecialNeeds())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            trip.setId(id);
            boolean success = tripService.update(trip);
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

    @PatchMapping("/{id}/img")
    public Map<String, Object> updateImg(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            String img = body != null ? body.get("img") : null;
            if (img == null) {
                result.put("success", false);
                result.put("message", "图片URL不能为空");
                return result;
            }
            boolean success = tripService.updateImg(id, img.trim());
            if (success) {
                result.put("success", true);
                result.put("message", "封面更新成功");
            } else {
                result.put("success", false);
                result.put("message", "封面更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除行程
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = tripService.deleteById(id);
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
     * 分页查询行程
     */
    @GetMapping("/page")
    public Map<String, Object> getPage(@RequestParam(required = false) Integer userId,
                                     @RequestParam(required = false) String status,
                                     @RequestParam(required = false) String destination,
                                     @RequestParam(defaultValue = "1") int pageNum,
                                     @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageInfo<Trip> pageInfo = tripService.getPage(userId, status, destination, pageNum, pageSize);
            
            // 为每个行程计算并更新查验进度
            for (Trip trip : pageInfo.getList()) {
                int totalItems = itemService.countByTripId(trip.getId());
                int checkedItems = itemService.countCheckedByTripId(trip.getId());
                int progress = totalItems > 0 ? (int) Math.round((double) checkedItems / totalItems * 100) : 0;
                
                trip.setTotalItems(totalItems);
                trip.setCheckedItems(checkedItems);
                trip.setProgress(progress);
            }
            
            result.put("success", true);
            result.put("data", pageInfo);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新行程进度
     */
    @PutMapping("/{id}/progress")
    public Map<String, Object> updateProgress(@PathVariable Integer id,
                                            @RequestParam Integer checkedItems,
                                            @RequestParam Integer totalItems,
                                            @RequestParam Integer progress) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = tripService.updateProgress(id, checkedItems, totalItems, progress);
            if (success) {
                result.put("success", true);
                result.put("message", "进度更新成功");
            } else {
                result.put("success", false);
                result.put("message", "进度更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "进度更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据状态查询行程
     */
    @GetMapping("/status/{status}")
    public Map<String, Object> getByStatus(@PathVariable String status) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Trip> trips = tripService.getByStatus(status);
            result.put("success", true);
            result.put("data", trips);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据目的地查询行程
     */
    @GetMapping("/destination/{destination}")
    public Map<String, Object> getByDestination(@PathVariable String destination) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Trip> trips = tripService.getByDestination(destination);
            result.put("success", true);
            result.put("data", trips);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取用户最近行程
     */
    @GetMapping("/user/{userId}/recent")
    public Map<String, Object> getRecentTrips(@PathVariable Integer userId,
                                             @RequestParam(defaultValue = "5") int limit) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Trip> trips = tripService.getRecentTrips(userId, limit);
            result.put("success", true);
            result.put("data", trips);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新行程状态
     */
    @PutMapping("/{id}/status")
    public Map<String, Object> updateStatus(@PathVariable Integer id, @RequestParam String status) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 验证状态值
            if (!isValidStatus(status)) {
                result.put("success", false);
                result.put("message", "无效的状态值，支持的状态：preparing, ongoing, completed, cancelled");
                return result;
            }
            
            boolean success = tripService.updateStatus(id, status);
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
     * 更新行程名称
     */
    @PatchMapping("/{id}/name")
    public Map<String, Object> updateName(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            String name = body != null ? body.get("name") : null;
            if (name == null || name.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "行程名称不能为空");
                return result;
            }
            boolean success = tripService.updateName(id, name.trim());
            if (success) {
                result.put("success", true);
                result.put("message", "名称更新成功");
            } else {
                result.put("success", false);
                result.put("message", "名称更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "名称更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 开始查验
     */
    @PostMapping("/{id}/check")
    public Map<String, Object> startCheck(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 获取当前行程状态
            Trip trip = tripService.getById(id);
            if (trip == null) {
                result.put("success", false);
                result.put("message", "行程不存在");
                return result;
            }
            
            // 如果是已完成状态，重置物品状态
            if ("completed".equals(trip.getStatus())) {
                // 重置所有物品的查验状态为0
                itemService.resetCheckedStatusByTripId(id);
            }
            
            // 更新状态为进行中
            boolean success = tripService.updateStatus(id, "ongoing");
            if (success) {
                result.put("success", true);
                result.put("message", "开始查验成功");
            } else {
                result.put("success", false);
                result.put("message", "开始查验失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "开始查验失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 暂停查验
     */
    @PostMapping("/{id}/pause")
    public Map<String, Object> pauseCheck(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 更新状态为准备中
            boolean success = tripService.updateStatus(id, "preparing");
            if (success) {
                result.put("success", true);
                result.put("message", "暂停查验成功");
            } else {
                result.put("success", false);
                result.put("message", "暂停查验失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "暂停查验失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 完成查验
     */
    @PostMapping("/{id}/complete")
    public Map<String, Object> completeCheck(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 获取物品统计信息
            int totalItems = itemService.countByTripId(id);
            int checkedItems = itemService.countCheckedByTripId(id);
            // 完成查验时，所有物品都已处理（查验或跳过），进度为100%
            int progress = 100;
            
            // 更新进度信息和状态
            tripService.updateProgress(id, checkedItems, totalItems, progress);
            boolean success = tripService.updateStatus(id, "completed");
            
            if (success) {
                result.put("success", true);
                result.put("message", "完成查验成功");
            } else {
                result.put("success", false);
                result.put("message", "完成查验失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "完成查验失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新查验进度
     */
    @PutMapping("/{id}/check")
    public Map<String, Object> updateCheckProgress(@PathVariable Integer id, @RequestBody Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 这个接口保留用于兼容性，但不再记录查验历史
            result.put("success", true);
            result.put("message", "操作成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 验证状态值是否有效
     */
    private boolean isValidStatus(String status) {
        return "preparing".equals(status) || "ongoing".equals(status) || 
               "completed".equals(status) || "cancelled".equals(status);
    }
}
