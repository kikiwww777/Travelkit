package com.example.check.cooperative.dto;

import lombok.Data;

/**
 * 成员完成度视图。
 */
@Data
public class MemberProgressDTO {
    private Integer memberId;
    private Integer tripId;
    private Integer userId;
    private String memberName;
    private String avatarUrl;
    private Boolean completed;
    private Integer checkedCount;
    private Integer pendingCount;
    private String pendingNames;
}


