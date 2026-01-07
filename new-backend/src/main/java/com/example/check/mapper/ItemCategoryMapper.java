package com.example.check.mapper;

import com.example.check.pojo.ItemCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物品分类Mapper接口
 */
@Mapper
public interface ItemCategoryMapper {
    
    /**
     * 根据ID查询分类
     */
    ItemCategory selectById(@Param("id") Integer id);
    
    /**
     * 根据代码查询分类
     */
    ItemCategory selectByCode(@Param("code") String code);
    
    /**
     * 查询所有分类
     */
    List<ItemCategory> selectAll();
    
    /**
     * 插入分类
     */
    int insert(ItemCategory category);
    
    /**
     * 更新分类
     */
    int update(ItemCategory category);
    
    /**
     * 删除分类
     */
    int deleteById(@Param("id") Integer id);
}
