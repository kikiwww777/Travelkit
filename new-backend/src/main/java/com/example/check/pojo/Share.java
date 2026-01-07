package com.example.check.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 分享实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Share {
    private Integer id;
    private Integer tripId;
    private String shareCode;
    private String viewPermission;
    private String editPermission;
    private String password;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryAt;
    
    private Boolean allowComments;
    private Integer views;
    private Integer shares;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
