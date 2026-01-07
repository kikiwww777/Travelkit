package com.example.check.mapper;

import com.example.check.pojo.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物品Mapper接口
 */
@Mapper
public interface ItemMapper {
    
    /**
     * 根据ID查询物品
     */
    Item selectById(@Param("id") Integer id);
    
    /**
     * 根据行程ID查询物品列表
     */
    List<Item> selectByTripId(@Param("tripId") Integer tripId);
    
    /**
     * 根据分类ID查询物品
     */
    List<Item> selectByCategoryId(@Param("categoryId") Integer categoryId);
    
    /**
     * 根据查验状态查询物品
     */
    List<Item> selectByCheckedStatus(@Param("tripId") Integer tripId, @Param("checked") Integer checked);
    
    /**
     * 插入物品
     */
    int insert(Item item);
    
    /**
     * 更新物品
     */
    int update(Item item);
    
    /**
     * 删除物品
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 批量插入物品
     */
    int batchInsert(@Param("items") List<Item> items);
    
    /**
     * 更新物品查验状态
     */
    int updateCheckedStatus(@Param("id") Integer id, @Param("checked") Integer checked);
    
    /**
     * 重置行程所有物品的查验状态为false
     */
    int resetCheckedStatusByTripId(@Param("tripId") Integer tripId);
    
    /**
     * 统计行程物品总数
     */
    int countByTripId(@Param("tripId") Integer tripId);
    
    /**
     * 统计已查验物品数
     */
    int countCheckedByTripId(@Param("tripId") Integer tripId);
    
    /**
     * 根据物品总览ID删除旅程物品
     */
    int deleteByItemOverviewId(@Param("tripId") Integer tripId, @Param("itemOverviewId") Integer itemOverviewId);
    
    /**
     * 根据物品总览ID查询旅程物品
     */
    Item selectByItemOverviewId(@Param("tripId") Integer tripId, @Param("itemOverviewId") Integer itemOverviewId);
    
    /**
     * 根据条件筛选物品（支持分类、状态、关键词筛选）
     */
    List<Item> selectFilteredItems(@Param("tripId") Integer tripId, 
                                   @Param("categoryCode") String categoryCode, 
                                   @Param("checked") Integer checked, 
                                   @Param("keyword") String keyword);
    
    /**
     * 根据行程ID删除所有物品（用于重置所有勾选状态）
     */
    int deleteByTripId(@Param("tripId") Integer tripId);
}
