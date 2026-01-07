package com.example.check.service.serviceImpl;

import com.example.check.mapper.UserMapper;
import com.example.check.mapper.TripMapper;
import com.example.check.pojo.User;
import com.example.check.pojo.Trip;
import com.example.check.service.UserService;
import com.example.check.util.WechatApiUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private TripMapper tripMapper;
    
    @Override
    public User getById(Integer id) {
        return userMapper.selectById(id);
    }

    
    @Override
    public boolean update(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userMapper.update(user) > 0;
    }
    
    @Override
    public boolean deleteById(Integer id) {
        return userMapper.deleteById(id) > 0;
    }
    
    @Override
    public PageInfo<User> getPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.selectAll();
        return new PageInfo<>(users);
    }
    
    @Override
    public List<User> getAll() {
        return userMapper.selectAll();
    }
    
    @Override
    public boolean isUsernameExists(String username) {
        return userMapper.selectByUsername(username) != null;
    }
    

    
    @Override
    public User getByOpenId(String openId) {
        return userMapper.selectByOpenId(openId);
    }
    
    @Override
    public User wechatLogin(String code, String appId) {
        // 第一步：通过微信API获取openId和sessionKey
        System.out.println("开始调用微信API，code: " + code + ", appId: " + appId);
        
        Map<String, String> wechatResult = WechatApiUtil.getOpenIdByCode(code, appId, null);
        
        if (wechatResult == null || !wechatResult.containsKey("openId")) {
            System.err.println("获取openId失败，无法继续登录流程");
            System.err.println("可能的原因：1. code已被使用（微信code只能使用一次）");
            System.err.println("           2. code已过期（code有效期约5分钟）");
            System.err.println("           3. appId或secret配置错误");
            System.err.println("           4. 网络请求失败");
            return null;
        }
        
        String openId = wechatResult.get("openId");
        String sessionKey = wechatResult.get("sessionKey");
        String unionId = wechatResult.get("unionId");
        
        System.out.println("成功获取openId: " + openId);
        if (sessionKey != null) {
            System.out.println("获取sessionKey成功");
        }
        if (unionId != null) {
            System.out.println("获取unionId: " + unionId);
        }
        
        // 第二步：先查询用户是否存在
        User user = userMapper.selectByOpenId(openId);

        
        if (user == null) {
            // 第三步：用户不存在，先进行注册
            user = new User();
            user.setOpenId(openId);
            
            // 生成唯一用户名，使用openId的前8位加上时间戳确保唯一性
            String timestamp = String.valueOf(System.currentTimeMillis());
            String baseUsername = "wx_" + openId.substring(0, Math.min(8, openId.length()));
            String username = baseUsername + "_" + timestamp.substring(timestamp.length() - 6);
            
            // 确保用户名唯一（如果已存在则追加随机数）
            int retryCount = 0;
            while (isUsernameExists(username) && retryCount < 5) {
                username = baseUsername + "_" + timestamp.substring(timestamp.length() - 6) + "_" + retryCount;
                retryCount++;
            }
            
            user.setUsername(username);
            // 设置微信名（name字段）
            user.setName("微信用户");
            // 设置微信头像（avatar字段）
            user.setAvatar("https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132");
            // 新用户默认状态为0（正常）
            user.setIsStatus(0);
            // 新用户默认身份为0（普通用户）
            user.setIdentity(0);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            // 执行注册（插入数据库）
            int insertResult = userMapper.insert(user);
            if (insertResult <= 0) {
                // 注册失败
                return null;
            }
            
            // 第三步：注册成功后，重新查询用户信息（获取生成的ID）
            user = userMapper.selectByOpenId(openId);
            if (user == null) {
                // 注册后查询失败
                return null;
            }
        } else {
            // 用户已存在，检查用户状态
            if (user.getIsStatus() != null && user.getIsStatus() == 1) {
                // 用户被封禁，返回null表示登录失败
                System.out.println("用户已被封禁，无法登录: " + user.getId());
                return null;
            }
        }
        
        // 第四步：返回用户信息（登录成功）
        return user;
    }
    
    @Override
    public Map<String, Object> getUserTripStatistics(Integer userId) {
        Map<String, Object> statistics = new HashMap<>();
        try {
            List<Trip> trips = tripMapper.selectByUserId(userId);
            
            int totalTrips = trips.size();
            int completedTrips = (int) trips.stream()
                    .filter(trip -> "completed".equalsIgnoreCase(trip.getStatus()))
                    .count();
            int totalDays = trips.stream()
                    .mapToInt(trip -> trip.getDuration() != null ? trip.getDuration() : 0)
                    .sum();
            
            statistics.put("totalTrips", totalTrips);
            statistics.put("completedTrips", completedTrips);
            statistics.put("totalDays", totalDays);
        } catch (Exception e) {
            System.err.println("获取用户行程统计失败: " + e.getMessage());
            statistics.put("totalTrips", 0);
            statistics.put("completedTrips", 0);
            statistics.put("totalDays", 0);
        }
        return statistics;
    }
}
