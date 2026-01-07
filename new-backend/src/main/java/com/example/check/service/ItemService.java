package com.example.check.service;

import com.example.check.pojo.Item;

import java.util.List;

/**
 * 物品服务接口
 */
public interface ItemService {
    
    /**
     * 根据ID查询物品
     */
    Item getById(Integer id);
    
    /**
     * 根据行程ID查询物品列表
     */
    List<Item> getByTripId(Integer tripId);
    
    /**
     * 添加物品
     */
    boolean add(Item item);
    
    /**
     * 批量添加物品
     */
    boolean batchAdd(List<Item> items);
    
    /**
     * 更新物品
     */
    boolean update(Item item);
    
    /**
     * 删除物品
     */
    boolean deleteById(Integer id);
    
    /**
     * 更新物品查验状态
     */
    boolean updateCheckedStatus(Integer itemId, Integer checked);
    
    /**
     * 重置行程所有物品的查验状态为false
     */
    boolean resetCheckedStatusByTripId(Integer tripId);
    
    /**
     * 根据分类查询物品
     */
    List<Item> getByCategory(Integer tripId, Integer categoryId);
    
    /**
     * 根据查验状态查询物品
     */
    List<Item> getByCheckedStatus(Integer tripId, Integer checked);
    
    /**
     * 统计行程物品总数
     */
    int countByTripId(Integer tripId);
    
    /**
     * 统计已查验物品数
     */
    int countCheckedByTripId(Integer tripId);
    
    /**
     * 从模板创建物品
     */
    boolean createFromTemplate(Integer tripId, Integer templateId);
    
    /**
     * 根据物品总览ID删除旅程物品
     */
    boolean deleteByItemOverviewId(Integer tripId, Integer itemOverviewId);
    
    /**
     * 根据物品总览ID查询旅程物品
     */
    Item getByItemOverviewId(Integer tripId, Integer itemOverviewId);
    
    /**
     * 根据条件筛选物品（支持分类、状态、关键词筛选）
     */
    List<Item> getFilteredItems(Integer tripId, String categoryCode, Integer checked, String keyword);
    
    /**
     * 批量删除行程中所有已勾选的物品（重置所有勾选状态）
     */
    boolean deleteAllCheckedItemsByTripId(Integer tripId);

    /**
     * 删除行程的所有物品
     */
    boolean deleteByTripId(Integer tripId);
}