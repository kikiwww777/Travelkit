package com.example.check.service;

import com.example.check.pojo.User;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 根据ID查询用户
     */
    User getById(Integer id);
    


    /**
     * 更新用户信息
     */
    boolean update(User user);
    
    /**
     * 删除用户
     */
    boolean deleteById(Integer id);
    
    /**
     * 分页查询用户
     */
    PageInfo<User> getPage(int pageNum, int pageSize);
    
    /**
     * 查询所有用户
     */
    List<User> getAll();
    
    /**
     * 验证用户名是否存在
     */
    boolean isUsernameExists(String username);
    

    /**
     * 根据openId查询用户
     */
    User getByOpenId(String openId);
    
    /**
     * 微信授权登录（如果用户不存在则自动注册）
     */
    User wechatLogin(String code, String appId);
    
    /**
     * 获取用户行程统计信息
     */
    Map<String, Object> getUserTripStatistics(Integer userId);
}
