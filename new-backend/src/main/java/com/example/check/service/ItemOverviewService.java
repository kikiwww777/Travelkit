package com.example.check.service;

import com.example.check.pojo.ItemOverview;

import java.util.List;

/**
 * 物品总览服务接口
 */
public interface ItemOverviewService {
    
    /**
     * 获取所有物品总览
     */
    List<ItemOverview> getAll();

    //管理员用
    List<ItemOverview> AdminGetAll();
    
    /**
     * 根据ID获取物品总览
     */
    ItemOverview getById(Integer id);
    
    /**
     * 根据分类ID获取物品总览
     */
    List<ItemOverview> getByCategoryId(Integer categoryId);
    
    /**
     * 根据名称搜索物品总览
     */
    List<ItemOverview> getByName(String name);
    
    /**
     * 添加物品总览
     */
    boolean add(ItemOverview itemOverview);
    
    /**
     * 更新物品总览
     */
    boolean update(ItemOverview itemOverview);
    
    /**
     * 删除物品总览
     */
    boolean deleteById(Integer id);
    
    /**
     * 统计物品总览数量
     */
    int count();
    
    /**
     * 根据分类统计物品总览数量
     */
    int countByCategoryId(Integer categoryId);
    
    /**
     * 根据条件筛选物品总览（支持分类、关键词筛选）
     */
    List<ItemOverview> getFilteredItems(String categoryCode, String keyword);
}


