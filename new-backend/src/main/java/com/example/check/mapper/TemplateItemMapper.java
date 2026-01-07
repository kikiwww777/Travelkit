package com.example.check.mapper;

import com.example.check.pojo.TemplateItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模板物品Mapper接口
 */
@Mapper
public interface TemplateItemMapper {
    
    /**
     * 根据模板ID查询模板物品列表
     */
    List<TemplateItem> selectByTemplateId(@Param("templateId") Integer templateId);
    
    /**
     * 根据ID查询模板物品
     */
    TemplateItem selectById(@Param("id") Integer id);
    
    /**
     * 新增模板物品
     */
    int insert(TemplateItem templateItem);
    
    /**
     * 更新模板物品
     */
    int update(TemplateItem templateItem);
    
    /**
     * 删除模板物品
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据模板ID删除所有模板物品
     */
    int deleteByTemplateId(@Param("templateId") Integer templateId);
}







