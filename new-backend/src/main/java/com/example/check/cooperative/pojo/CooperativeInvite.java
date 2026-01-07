package com.example.check.cooperative.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 合作行程邀请记录。
 */
@Data
public class CooperativeInvite {
    private Integer id;
    private Integer tripId;
    private Integer createdBy;
    private String inviteType; // QR / CODE / WECHAT
    private String token;
    private String shortCode;
    private String shareLink;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiredAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}



