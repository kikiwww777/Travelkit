package com.example.check.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户偏好实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferences {
    private Integer id;
    private Integer userId;
    private String transport;
    private String accommodation;
    private String activities;
    private String specialNeeds;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
