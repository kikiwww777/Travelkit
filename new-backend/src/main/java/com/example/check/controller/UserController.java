package com.example.check.controller;

import com.example.check.pojo.User;
import com.example.check.service.UserService;
import com.example.check.util.SensitiveWordFilter;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 获取用户行程统计信息
     */
    @GetMapping("/{id}/statistics")
    public Map<String, Object> getUserStatistics(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> statistics = userService.getUserTripStatistics(id);
            result.put("success", true);
            result.put("data", statistics);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取统计信息失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            User user = userService.getById(id);
            if (user != null) {
                result.put("success", true);
                result.put("data", user);
            } else {
                result.put("success", false);
                result.put("message", "用户不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Integer id, @RequestBody Map<String, Object> updateData) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 获取现有用户信息
            User existingUser = userService.getById(id);
            if (existingUser == null) {
                result.put("success", false);
                result.put("message", "用户不存在");
                return result;
            }

            // 更新字段（只更新提供的字段）
            if (updateData.containsKey("name")) {
                String name = updateData.get("name") != null ? updateData.get("name").toString() : null;
                if (name != null && SensitiveWordFilter.containsSensitiveWord(name)) {
                    result.put("success", false);
                    result.put("message", "请勿输入敏感内容");
                    return result;
                }
                existingUser.setName(name);
            }
            if (updateData.containsKey("gender")) {
                existingUser.setGender(updateData.get("gender") != null ? updateData.get("gender").toString() : null);
            }
            if (updateData.containsKey("birthday")) {
                String birthdayStr = updateData.get("birthday") != null ? updateData.get("birthday").toString() : null;
                if (birthdayStr != null && !birthdayStr.isEmpty()) {
                    try {
                        existingUser.setBirthday(java.time.LocalDate.parse(birthdayStr));
                    } catch (Exception e) {
                        result.put("success", false);
                        result.put("message", "生日格式错误，请使用yyyy-MM-dd格式");
                        return result;
                    }
                } else {
                    existingUser.setBirthday(null);
                }
            }
            if (updateData.containsKey("avatar")) {
                existingUser.setAvatar(updateData.get("avatar") != null ? updateData.get("avatar").toString() : null);
            }
            if (updateData.containsKey("identity")) {
                Object identityObj = updateData.get("identity");
                if (identityObj != null) {
                    Integer identity = identityObj instanceof Integer 
                        ? (Integer) identityObj 
                        : Integer.parseInt(identityObj.toString());
                    existingUser.setIdentity(identity);
                }
            }
            if (updateData.containsKey("bio")) {
                String bio = updateData.get("bio") != null ? updateData.get("bio").toString() : null;
                if (bio != null && SensitiveWordFilter.containsSensitiveWord(bio)) {
                    result.put("success", false);
                    result.put("message", "请勿输入敏感内容");
                    return result;
                }
                existingUser.setBio(bio);
            }
            if (updateData.containsKey("isStatus")) {
                Object isStatus = updateData.get("isStatus");
                if (isStatus != null) {
                    Integer status = isStatus instanceof Integer
                            ? (Integer) isStatus
                            : Integer.parseInt(isStatus.toString());
                    existingUser.setIsStatus(status);
                }
            }

            boolean success = userService.update(existingUser);
            if (success) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = userService.deleteById(id);
            if (success) {
                result.put("success", true);
                result.put("message", "删除成功");
            } else {
                result.put("success", false);
                result.put("message", "删除失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    public Map<String, Object> getPage(@RequestParam(defaultValue = "1") int pageNum, 
                                     @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageInfo<User> pageInfo = userService.getPage(pageNum, pageSize);
            result.put("success", true);
            result.put("data", pageInfo);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询所有用户
     */
    @GetMapping("/all")
    public Map<String, Object> getAll() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> users = userService.getAll();
            result.put("success", true);
            result.put("data", users);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 微信授权登录
     * 接收微信小程序的code、用户昵称和头像
     * 如果用户首次登录，先进行注册，然后再登录
     */
    @PostMapping("/wechat-login")
    public Map<String, Object> wechatLogin(@RequestBody Map<String, Object> loginData) {
        System.out.println("接收到的登录数据: " + loginData);
        Map<String, Object> result = new HashMap<>();
        try {
            String code = loginData.get("code") != null ? loginData.get("code").toString() : null;
            String appId = loginData.get("appId") != null ? loginData.get("appId").toString() : null;
            
            System.out.println("解析后的参数 - code: " + code);
             System.out.println("解析后的参数 - appId: " + appId);
            
            if (code == null || code.isEmpty()) {
                result.put("success", false);
                result.put("message", "code不能为空");
                return result;
            }
            
            // 打印接收到的appId
            System.out.println("接收到的appId: " + appId);
            
            // 调用微信登录服务（内部会先通过微信API获取openId，然后检查用户是否存在，如果不存在则先注册再登录）
            // 注意：code只能使用一次，所以只在这里调用一次微信API
            User user = userService.wechatLogin(code,  appId);
            
            if (user != null) {
                // 再次检查用户状态（双重保险）
                if (user.getIsStatus() != null && user.getIsStatus() == 1) {
                    result.put("success", false);
                    result.put("message", "您的账号已被封禁，无法登录");
                    return result;
                }
                // 判断是否为新用户（通过检查用户创建时间来判断）
                boolean isNewUser = false;
                if (user.getCreatedAt() != null) {
                    // 如果创建时间在2分钟内，认为是新用户（考虑到网络延迟等因素，放宽到2分钟）
                    long createTime = user.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
                    long now = System.currentTimeMillis();
                    isNewUser = (now - createTime) < 120000; // 2分钟内
                }
                
                result.put("success", true);
                if (isNewUser) {
                    result.put("message", "注册并登录成功");
                } else {
                    result.put("message", "登录成功");
                }
                result.put("data", user);
                result.put("isNewUser", isNewUser); // 标识是否为新用户
            } else {
                result.put("success", false);
                result.put("message", "登录失败：无法获取微信用户信息，请重试");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "登录失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取游客用户（open_id='userUse'）
     */
    @GetMapping("/guest")
    public Map<String, Object> getGuestUser() {
        Map<String, Object> result = new HashMap<>();
        try {
            User user = userService.getByOpenId("userUse");
            if (user != null) {
                result.put("success", true);
                result.put("data", user);
            } else {
                result.put("success", false);
                result.put("message", "游客用户不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取游客用户失败：" + e.getMessage());
        }
        return result;
    }
}