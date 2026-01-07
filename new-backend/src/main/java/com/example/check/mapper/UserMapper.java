package com.example.check.mapper;

import com.example.check.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据ID查询用户
     */
    User selectById(@Param("id") Integer id);
    
    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);
    
    /**
     * 根据手机号查询用户
     */
    User selectByPhone(@Param("phone") String phone);
    
    /**
     * 根据邮箱查询用户
     */
    User selectByEmail(@Param("email") String email);
    
    /**
     * 根据openId查询用户
     */
    User selectByOpenId(@Param("openId") String openId);
    
    /**
     * 插入用户
     */
    int insert(User user);
    
    /**
     * 更新用户信息
     */
    int update(User user);
    
    /**
     * 删除用户
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 查询所有用户
     */
    List<User> selectAll();
    
    /**
     * 分页查询用户
     */
    List<User> selectByPage(@Param("offset") Integer offset, @Param("limit") Integer limit);
    
    /**
     * 统计用户总数
     */
    int count();
}
