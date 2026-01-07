package com.example.check.mapper;

import com.example.check.pojo.Collaborator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 协作者Mapper接口
 */
@Mapper
public interface CollaboratorMapper {
    
    /**
     * 根据ID查询协作者
     */
    Collaborator selectById(@Param("id") Integer id);
    
    /**
     * 根据分享ID查询协作者列表
     */
    List<Collaborator> selectByShareId(@Param("shareId") Integer shareId);
    
    /**
     * 根据用户ID查询协作者列表
     */
    List<Collaborator> selectByUserId(@Param("userId") Integer userId);
    
    /**
     * 插入协作者
     */
    int insert(Collaborator collaborator);
    
    /**
     * 更新协作者
     */
    int update(Collaborator collaborator);
    
    /**
     * 删除协作者
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据分享ID和用户ID删除协作者
     */
    int deleteByShareIdAndUserId(@Param("shareId") Integer shareId, @Param("userId") Integer userId);
}
