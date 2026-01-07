package com.example.check.mapper;

import com.example.check.pojo.Trip;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 行程Mapper接口
 */
@Mapper
public interface TripMapper {
    
    /**
     * 根据ID查询行程
     */
    Trip selectById(@Param("id") Integer id);
    
    /**
     * 根据用户ID查询行程列表
     */
    List<Trip> selectByUserId(@Param("userId") Integer userId);
    
    /**
     * 根据状态查询行程
     */
    List<Trip> selectByStatus(@Param("status") String status);
    
    /**
     * 根据目的地查询行程
     */
    List<Trip> selectByDestination(@Param("destination") String destination);
    
    /**
     * 插入行程
     */
    int insert(Trip trip);
    
    /**
     * 更新行程
     */
    int update(Trip trip);
    
    /**
     * 删除行程
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 更新行程进度
     */
    int updateProgress(@Param("id") Integer id, @Param("checkedItems") Integer checkedItems, 
                      @Param("totalItems") Integer totalItems, @Param("progress") Integer progress);
    
    /**
     * 分页查询行程
     */
    List<Trip> selectByPage(@Param("userId") Integer userId, @Param("status") String status, 
                           @Param("destination") String destination, @Param("offset") Integer offset, 
                           @Param("limit") Integer limit);
    
    /**
     * 统计行程总数
     */
    int count(@Param("userId") Integer userId, @Param("status") String status, 
              @Param("destination") String destination);
    
    /**
     * 更新行程状态
     */
    int updateStatus(@Param("id") Integer id, @Param("status") String status);

    /**
     * 更新行程名称
     */
    int updateName(@Param("id") Integer id, @Param("name") String name);

    /**
     * 更新行程封面
     */
    int updateImg(@Param("id") Integer id, @Param("img") String img);
}
