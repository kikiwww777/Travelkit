package com.example.check.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信API工具类
 * 用于调用微信小程序相关API
 */
public class WechatApiUtil {
    
    // 微信小程序配置
    private static final String WECHAT_APP_ID = "wxa728a530a0684325";
    private static final String WECHAT_SECRET = "ceccc67123f49954b25543bbde82a25a";
    private static final String WECHAT_API_URL = "https://api.weixin.qq.com/sns/jscode2session";
    
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 通过code获取openId和sessionKey
     * 
     * @param code 微信登录code
     * @return 包含openId和sessionKey的Map，如果失败返回null
     */
    public static Map<String, String> getOpenIdByCode(String code) {
        try {
            // 构建请求URL
            String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    WECHAT_API_URL, WECHAT_APP_ID, WECHAT_SECRET, code);
            
            // 发送GET请求
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    url, 
                    HttpMethod.GET, 
                    entity, 
                    String.class
            );
            
            // 解析响应
            String responseBody = response.getBody();
            System.out.println("微信API响应: " + responseBody);
            
            if (responseBody == null || responseBody.isEmpty()) {
                System.err.println("微信API响应为空");
                return null;
            }
            
            // 解析JSON
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            // 检查是否有错误
            if (jsonNode.has("errcode")) {
                int errcode = jsonNode.get("errcode").asInt();
                String errmsg = jsonNode.has("errmsg") ? jsonNode.get("errmsg").asText() : "未知错误";
                System.err.println("微信API错误: errcode=" + errcode + ", errmsg=" + errmsg);
                
                // 特殊处理40163错误（code已被使用）
                if (errcode == 40163) {
                    System.err.println("错误详情: code已被使用或已过期，请重新获取code");
                }
                
                return null;
            }
            
            // 提取openId和sessionKey
            Map<String, String> result = new HashMap<>();
            if (jsonNode.has("openid")) {
                result.put("openId", jsonNode.get("openid").asText());
            }
            if (jsonNode.has("session_key")) {
                result.put("sessionKey", jsonNode.get("session_key").asText());
            }
            if (jsonNode.has("unionid")) {
                result.put("unionId", jsonNode.get("unionid").asText());
            }
            
            return result;
            
        } catch (Exception e) {
            System.err.println("调用微信API失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 通过code和自定义的appId、secret获取openId
     * 
     * @param code 微信登录code
     * @param appId 小程序appId（如果传入则使用，否则使用默认配置）
     * @param secret 小程序secret（如果传入则使用，否则使用默认配置）
     * @return 包含openId和sessionKey的Map，如果失败返回null
     */
    public static Map<String, String> getOpenIdByCode(String code, String appId, String secret) {
        try {
            // 使用传入的appId和secret，如果为空则使用默认配置
            String finalAppId = (appId != null && !appId.isEmpty()) ? appId : WECHAT_APP_ID;
            String finalSecret = (secret != null && !secret.isEmpty()) ? secret : WECHAT_SECRET;
            
            // 构建请求URL
            String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    WECHAT_API_URL, finalAppId, finalSecret, code);
            
            // 发送GET请求
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    url, 
                    HttpMethod.GET, 
                    entity, 
                    String.class
            );
            
            // 解析响应
            String responseBody = response.getBody();
            System.out.println("微信API响应 (appId=" + finalAppId + "): " + responseBody);
            
            if (responseBody == null || responseBody.isEmpty()) {
                System.err.println("微信API响应为空");
                return null;
            }
            
            // 解析JSON
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            // 检查是否有错误
            if (jsonNode.has("errcode")) {
                int errcode = jsonNode.get("errcode").asInt();
                String errmsg = jsonNode.has("errmsg") ? jsonNode.get("errmsg").asText() : "未知错误";
                System.err.println("微信API错误 (appId=" + finalAppId + "): errcode=" + errcode + ", errmsg=" + errmsg);
                
                // 特殊处理40163错误（code已被使用）
                if (errcode == 40163) {
                    System.err.println("错误详情: code已被使用或已过期，请重新获取code");
                }
                
                return null;
            }
            
            // 提取openId和sessionKey
            Map<String, String> result = new HashMap<>();
            if (jsonNode.has("openid")) {
                result.put("openId", jsonNode.get("openid").asText());
            }
            if (jsonNode.has("session_key")) {
                result.put("sessionKey", jsonNode.get("session_key").asText());
            }
            if (jsonNode.has("unionid")) {
                result.put("unionId", jsonNode.get("unionid").asText());
            }
            
            return result;
            
        } catch (Exception e) {
            System.err.println("调用微信API失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

