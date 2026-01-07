package com.example.check.service;

import com.example.check.pojo.Trip;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 行程服务接口
 */
public interface TripService {
    
    /**
     * 根据ID查询行程
     */
    Trip getById(Integer id);
    
    /**
     * 根据用户ID查询行程列表
     */
    List<Trip> getByUserId(Integer userId);
    
    /**
     * 创建行程
     */
    boolean create(Trip trip);
    
    /**
     * 更新行程
     */
    boolean update(Trip trip);
    
    /**
     * 删除行程
     */
    boolean deleteById(Integer id);
    
    /**
     * 分页查询行程
     */
    PageInfo<Trip> getPage(Integer userId, String status, String destination, int pageNum, int pageSize);
    
    /**
     * 更新行程进度
     */
    boolean updateProgress(Integer tripId, Integer checkedItems, Integer totalItems, Integer progress);
    
    /**
     * 根据状态查询行程
     */
    List<Trip> getByStatus(String status);
    
    /**
     * 根据目的地查询行程
     */
    List<Trip> getByDestination(String destination);
    
    /**
     * 获取用户最近行程
     */
    List<Trip> getRecentTrips(Integer userId, int limit);
    
    /**
     * 更新行程状态
     */
    boolean updateStatus(Integer tripId, String status);

    /**
     * 更新行程名称
     */
    boolean updateName(Integer tripId, String name);

    /**
     * 更新行程封面
     */
    boolean updateImg(Integer tripId, String img);
}
