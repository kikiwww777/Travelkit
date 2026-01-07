package com.example.check.mapper;

import com.example.check.pojo.Reminder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 智能提醒Mapper接口
 */
@Mapper
public interface ReminderMapper {
    
    /**
     * 根据ID查询提醒
     */
    Reminder selectById(@Param("id") Integer id);
    
    /**
     * 根据行程ID查询提醒列表
     */
    List<Reminder> selectByTripId(@Param("tripId") Integer tripId);
    
    /**
     * 根据类型查询提醒
     */
    List<Reminder> selectByType(@Param("type") String type);
    
    /**
     * 查询未发送的提醒
     */
    List<Reminder> selectUnsentReminders();
    
    /**
     * 插入提醒
     */
    int insert(Reminder reminder);
    
    /**
     * 更新提醒
     */
    int update(Reminder reminder);
    
    /**
     * 删除提醒
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 标记提醒为已发送
     */
    int markAsSent(@Param("id") Integer id);
}
