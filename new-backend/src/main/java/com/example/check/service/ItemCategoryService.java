package com.example.check.service;

import com.example.check.pojo.ItemCategory;

import java.util.List;

/**
 * 物品分类服务接口
 */
public interface ItemCategoryService {
    
    /**
     * 根据ID查询分类
     */
    ItemCategory getById(Integer id);
    
    /**
     * 根据代码查询分类
     */
    ItemCategory getByCode(String code);
    
    /**
     * 查询所有分类
     */
    List<ItemCategory> getAll();
    
    /**
     * 添加分类
     */
    boolean add(ItemCategory category);
    
    /**
     * 更新分类
     */
    boolean update(ItemCategory category);
    
    /**
     * 删除分类
     */
    boolean deleteById(Integer id);
}
