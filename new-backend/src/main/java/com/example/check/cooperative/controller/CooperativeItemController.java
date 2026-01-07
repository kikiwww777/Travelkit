package com.example.check.cooperative.controller;

import com.example.check.cooperative.pojo.CooperativeItem;
import com.example.check.cooperative.pojo.CooperativeTrip;
import com.example.check.cooperative.service.CooperativeItemService;
import com.example.check.cooperative.service.CooperativeTripService;
import com.example.check.util.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cooperative/items")
@CrossOrigin(origins = "*")
public class CooperativeItemController {

    @Autowired
    private CooperativeItemService itemService;
    
    @Autowired
    private CooperativeTripService tripService;

    @GetMapping("/trip/{tripId}")
    public Map<String, Object> listByTrip(@PathVariable Integer tripId,
                                         @RequestParam(required = false) Integer userId) {
        List<CooperativeItem> items = itemService.listByTrip(tripId, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", items);
        return result;
    }

    @PostMapping
    public Map<String, Object> addItem(@RequestBody CooperativeItem item) {
        Map<String, Object> result = new HashMap<>();
        if (item == null || item.getTripId() == null) {
            result.put("success", false);
            result.put("message", "缺少行程信息，无法添加物品");
            return result;
        }
        CooperativeTrip trip = tripService.getById(item.getTripId(), null);
        if (trip == null) {
            result.put("success", false);
            result.put("message", "行程不存在，无法添加物品");
            return result;
        }
        String status = trip.getStatus() != null ? trip.getStatus().toLowerCase() : "";
        if ("cancelled".equals(status)) {
            result.put("success", false);
            result.put("message", "行程已取消，无法再添加物品");
            return result;
        }
        // 敏感词检测
        if (item.getName() != null && SensitiveWordFilter.containsSensitiveWord(item.getName())) {
            result.put("success", false);
            result.put("message", "请勿输入敏感内容");
            return result;
        }
        if (item.getDescription() != null && SensitiveWordFilter.containsSensitiveWord(item.getDescription())) {
            result.put("success", false);
            result.put("message", "请勿输入敏感内容");
            return result;
        }
        boolean success = itemService.addItem(item);
        result.put("success", success);
        result.put("message", success ? "添加成功" : "添加失败");
        return result;
    }

    @PutMapping("/{id}/check")
    public Map<String, Object> updateChecked(@PathVariable Integer id,
                                             @RequestBody Map<String, Object> payload) {
        Integer checked = extractInteger(payload != null ? payload.get("checked") : null);
        Integer userId = payload != null && payload.get("userId") != null ? 
            Integer.valueOf(payload.get("userId").toString()) : null;
        Integer tripId = payload != null && payload.get("tripId") != null ? 
            Integer.valueOf(payload.get("tripId").toString()) : null;
        String scope = payload != null && payload.get("scope") != null ? 
            payload.get("scope").toString() : "self";
        
        Map<String, Object> result = new HashMap<>();
        if (checked == null) {
            result.put("success", false);
            result.put("message", "缺少checked参数");
            return result;
        }
        if (userId == null) {
            result.put("success", false);
            result.put("message", "缺少userId参数");
            return result;
        }
        boolean success = itemService.updateChecked(id, checked, userId, scope, tripId);
        result.put("success", success);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    private Integer extractInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof Boolean) {
            return ((Boolean) value) ? 1 : 0;
        }
        if (value instanceof String) {
            try {
                return Integer.valueOf((String) value);
            } catch (NumberFormatException e) {
                String text = ((String) value).trim().toLowerCase();
                if ("true".equals(text)) {
                    return 1;
                }
                if ("false".equals(text)) {
                    return 0;
                }
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        boolean success = itemService.deleteItem(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    @DeleteMapping("/trip/{tripId}/checked/reset")
    public Map<String, Object> resetChecked(@PathVariable Integer tripId,
                                           @RequestParam(required = false) Integer userId) {
        boolean success = itemService.resetChecked(tripId, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "重置成功" : "重置失败");
        return result;
    }

    @DeleteMapping("/{itemId}/check")
    public Map<String, Object> deleteCheckRecord(@PathVariable Integer itemId,
                                                 @RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        boolean success = itemService.deleteCheckRecord(itemId, userId);
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
}

