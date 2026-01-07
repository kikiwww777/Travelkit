package com.example.check.controller;

import com.example.check.mapper.TemplateItemMapper;
import com.example.check.pojo.TemplateItem;
import com.example.check.service.TripTemplateService;
import com.example.check.util.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板物品控制器
 */
@RestController
@RequestMapping("/api/template-items")
@CrossOrigin(origins = "*")
public class TemplateItemController {
    
    @Autowired
    private TemplateItemMapper templateItemMapper;
    
    @Autowired
    private TripTemplateService tripTemplateService;
    
    /**
     * 根据ID查询模板物品
     */
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            TemplateItem templateItem = templateItemMapper.selectById(id);
            if (templateItem != null) {
                result.put("success", true);
                result.put("data", templateItem);
            } else {
                result.put("success", false);
                result.put("message", "模板物品不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据模板ID查询模板物品列表
     */
    @GetMapping("/template/{templateId}")
    public Map<String, Object> getByTemplateId(@PathVariable Integer templateId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TemplateItem> templateItems = templateItemMapper.selectByTemplateId(templateId);
            result.put("success", true);
            result.put("data", templateItems);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 新增模板物品（仅模板创建者可操作）
     */
    @PostMapping
    public Map<String, Object> create(@RequestParam Integer userId, @RequestBody TemplateItem templateItem) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (templateItem.getTemplateId() == null) {
                result.put("success", false);
                result.put("message", "模板ID不能为空");
                return result;
            }
            if (!tripTemplateService.isOwner(templateItem.getTemplateId(), userId)) {
                result.put("success", false);
                result.put("message", "仅模板创建者可编辑模板内容");
                return result;
            }
            // 敏感词检测
            if (templateItem.getName() != null && SensitiveWordFilter.containsSensitiveWord(templateItem.getName())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (templateItem.getNote() != null && SensitiveWordFilter.containsSensitiveWord(templateItem.getNote())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (templateItem.getPriority() == null || templateItem.getPriority().isEmpty()) {
                templateItem.setPriority("medium");
            }
            boolean success = templateItemMapper.insert(templateItem) > 0;
            if (success) {
                result.put("success", true);
                result.put("message", "新增成功");
                result.put("data", templateItem);
            } else {
                result.put("success", false);
                result.put("message", "新增失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "新增失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新模板物品（仅模板创建者可操作）
     */
    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Integer id,
                                    @RequestParam Integer userId,
                                    @RequestBody TemplateItem templateItem) {
        Map<String, Object> result = new HashMap<>();
        try {
            TemplateItem existing = templateItemMapper.selectById(id);
            if (existing == null) {
                result.put("success", false);
                result.put("message", "模板物品不存在");
                return result;
            }
            if (!tripTemplateService.isOwner(existing.getTemplateId(), userId)) {
                result.put("success", false);
                result.put("message", "仅模板创建者可编辑模板内容");
                return result;
            }
            // 敏感词检测
            if (templateItem.getName() != null && SensitiveWordFilter.containsSensitiveWord(templateItem.getName())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (templateItem.getNote() != null && SensitiveWordFilter.containsSensitiveWord(templateItem.getNote())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            templateItem.setId(id);
            templateItem.setTemplateId(existing.getTemplateId()); // 不允许修改归属模板
            boolean success = templateItemMapper.update(templateItem) > 0;
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
     * 删除模板物品（仅模板创建者可操作）
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Integer id, @RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            TemplateItem existing = templateItemMapper.selectById(id);
            if (existing == null) {
                result.put("success", false);
                result.put("message", "模板物品不存在");
                return result;
            }
            if (!tripTemplateService.isOwner(existing.getTemplateId(), userId)) {
                result.put("success", false);
                result.put("message", "仅模板创建者可编辑模板内容");
                return result;
            }
            boolean success = templateItemMapper.deleteById(id) > 0;
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
}







