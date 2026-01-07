package com.example.check.service;

import com.example.check.pojo.TripTemplate;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 行程模板服务接口
 */
public interface TripTemplateService {
    
    /**
     * 根据ID查询模板
     */
    TripTemplate getById(Integer id);
    
    /**
     * 查询所有公开模板
     */
    List<TripTemplate> getPublicTemplates();
    
    /**
     * 根据创建者查询模板
     */
    List<TripTemplate> getByCreatedBy(Integer createdBy);
    
    /**
     * 创建模板
     */
    boolean create(TripTemplate template);
    
    /**
     * 更新模板
     */
    boolean update(TripTemplate template);
    
    /**
     * 删除模板
     */
    boolean deleteById(Integer id);
    
    /**
     * 分页查询模板
     */
    PageInfo<TripTemplate> getPage(Boolean isPublic, String type, String destination, int pageNum, int pageSize);
    
    /**
     * 根据类型查询模板
     */
    List<TripTemplate> getByType(String type);
    
    /**
     * 根据目的地查询模板
     */
    List<TripTemplate> getByDestination(String destination);
    
    /**
     * 判断用户是否为模板创建者
     */
    boolean isOwner(Integer templateId, Integer userId);
    
    /**
     * 查询所有模板（管理员用）
     */
    List<TripTemplate> getAllTemplates();
    
    /**
     * 审核通过模板
     */
    boolean approveTemplate(Integer id, String result);
    
    /**
     * 审核拒绝模板
     */
    boolean rejectTemplate(Integer id, String result);
    
}
