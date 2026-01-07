package com.example.check.controller;

import com.example.check.pojo.TripTemplate;
import com.example.check.service.TripTemplateService;
import com.example.check.util.SensitiveWordFilter;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行程模板控制器
 */
@RestController
@RequestMapping("/api/templates")
@CrossOrigin(origins = "*")
public class TripTemplateController {
    
    @Autowired
    private TripTemplateService tripTemplateService;
    
    /**
     * 根据ID查询模板
     */
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Integer id,
                                     @RequestParam(required = false) Integer userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            TripTemplate template = tripTemplateService.getById(id);
            if (template != null) {
                if (!Boolean.TRUE.equals(template.getIsPublic())
                        && !tripTemplateService.isOwner(id, userId)) {
                    result.put("success", false);
                    result.put("message", "该模板仅创建者可查看");
                    return result;
                }
                result.put("success", true);
                result.put("data", template);
            } else {
                result.put("success", false);
                result.put("message", "模板不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询所有公开模板
     */
    @GetMapping("/public")
    public Map<String, Object> getPublicTemplates() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TripTemplate> templates = tripTemplateService.getPublicTemplates();
            result.put("success", true);
            result.put("data", templates);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据创建者查询模板
     */
    @GetMapping("/user/{createdBy}")
    public Map<String, Object> getByCreatedBy(@PathVariable Integer createdBy,
                                            @RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (!createdBy.equals(userId)) {
                result.put("success", false);
                result.put("message", "只能查看自己的模板");
                return result;
            }
            List<TripTemplate> templates = tripTemplateService.getByCreatedBy(createdBy);
            result.put("success", true);
            result.put("data", templates);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 创建模板
     */
    @PostMapping
    public Map<String, Object> create(@RequestParam Integer userId, @RequestBody TripTemplate template) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 敏感词检测
            if (template.getName() != null && SensitiveWordFilter.containsSensitiveWord(template.getName())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (template.getDescription() != null && SensitiveWordFilter.containsSensitiveWord(template.getDescription())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            template.setCreatedBy(userId);
            if (template.getIsPublic() == null) {
                template.setIsPublic(false);
            }
            boolean success = tripTemplateService.create(template);
            if (success) {
                result.put("success", true);
                result.put("message", "创建成功");
                result.put("data", template);
            } else {
                result.put("success", false);
                result.put("message", "创建失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新模板
     */
    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Integer id,
                                    @RequestParam Integer userId,
                                    @RequestBody TripTemplate template) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (!tripTemplateService.isOwner(id, userId)) {
                result.put("success", false);
                result.put("message", "仅模板创建者可以编辑");
                return result;
            }
            // 敏感词检测
            if (template.getName() != null && SensitiveWordFilter.containsSensitiveWord(template.getName())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (template.getDescription() != null && SensitiveWordFilter.containsSensitiveWord(template.getDescription())) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            template.setId(id);
            // 不修改createdBy，保持原创建者
            TripTemplate existing = tripTemplateService.getById(id);
            if (existing != null) {
                template.setCreatedBy(existing.getCreatedBy());
            }
            // 如果修改为公开模板，isStatus改为0（需要重新审核）
            if (template.getIsPublic() != null && template.getIsPublic()) {
                template.setIsStatus(0);
            }
            boolean success = tripTemplateService.update(template);
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
     * 删除模板
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Integer id, @RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (!tripTemplateService.isOwner(id, userId)) {
                result.put("success", false);
                result.put("message", "仅模板创建者可以删除");
                return result;
            }
            boolean success = tripTemplateService.deleteById(id);
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
     * 分页查询模板
     */
    @GetMapping("/page")
    public Map<String, Object> getPage(@RequestParam(required = false) Boolean isPublic,
                                     @RequestParam(required = false) String type,
                                     @RequestParam(required = false) String destination,
                                     @RequestParam(defaultValue = "1") int pageNum,
                                     @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageInfo<TripTemplate> pageInfo = tripTemplateService.getPage(isPublic, type, destination, pageNum, pageSize);
            result.put("success", true);
            result.put("data", pageInfo);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据类型查询模板
     */
    @GetMapping("/type/{type}")
    public Map<String, Object> getByType(@PathVariable String type) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TripTemplate> templates = tripTemplateService.getByType(type);
            result.put("success", true);
            result.put("data", templates);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 根据目的地查询模板
     */
    @GetMapping("/destination/{destination}")
    public Map<String, Object> getByDestination(@PathVariable String destination) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TripTemplate> templates = tripTemplateService.getByDestination(destination);
            result.put("success", true);
            result.put("data", templates);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询所有模板（管理员用，包括公开和私有）
     */
    @GetMapping("/all")
    public Map<String, Object> getAllTemplates() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<TripTemplate> templates = tripTemplateService.getAllTemplates();
            result.put("success", true);
            result.put("data", templates);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 审核通过模板
     */
    @PutMapping("/{id}/approve")
    public Map<String, Object> approveTemplate(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String auditResult = request.get("result");
            if (auditResult == null || auditResult.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "审核原因不能为空");
                return result;
            }
            boolean success = tripTemplateService.approveTemplate(id, auditResult.trim());
            if (success) {
                result.put("success", true);
                result.put("message", "审核通过成功");
            } else {
                result.put("success", false);
                result.put("message", "审核失败，模板不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "审核失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 审核拒绝模板
     */
    @PutMapping("/{id}/reject")
    public Map<String, Object> rejectTemplate(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String auditResult = request.get("result");
            if (auditResult == null || auditResult.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "审核原因不能为空");
                return result;
            }
            boolean success = tripTemplateService.rejectTemplate(id, auditResult.trim());
            if (success) {
                result.put("success", true);
                result.put("message", "拒绝成功");
            } else {
                result.put("success", false);
                result.put("message", "操作失败，模板不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败：" + e.getMessage());
        }
        return result;
    }
}