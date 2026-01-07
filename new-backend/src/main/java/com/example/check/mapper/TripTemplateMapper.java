package com.example.check.mapper;

import com.example.check.pojo.TripTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 行程模板Mapper接口
 */
@Mapper
public interface TripTemplateMapper {
    
    /**
     * 根据ID查询模板
     */
    TripTemplate selectById(@Param("id") Integer id);
    
    /**
     * 查询所有公开模板
     */
    List<TripTemplate> selectPublicTemplates();
    
    /**
     * 根据创建者查询模板
     */
    List<TripTemplate> selectByCreatedBy(@Param("createdBy") Integer createdBy);
    
    /**
     * 根据类型查询模板
     */
    List<TripTemplate> selectByType(@Param("type") String type);
    
    /**
     * 根据目的地查询模板
     */
    List<TripTemplate> selectByDestination(@Param("destination") String destination);
    
    /**
     * 插入模板
     */
    int insert(TripTemplate template);
    
    /**
     * 更新模板
     */
    int update(TripTemplate template);
    
    /**
     * 删除模板
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 分页查询模板
     */
    List<TripTemplate> selectByPage(@Param("isPublic") Boolean isPublic, @Param("type") String type, 
                                   @Param("destination") String destination, @Param("offset") Integer offset, 
                                   @Param("limit") Integer limit);
    
    /**
     * 统计模板总数
     */
    int count(@Param("isPublic") Boolean isPublic, @Param("type") String type, 
              @Param("destination") String destination);
    
    /**
     * 查询所有模板（管理员用）
     */
    List<TripTemplate> selectAll();
}
