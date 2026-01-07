package com.example.check.cooperative.pojo;

import lombok.Data;

/**
 * 合作行程成员信息。
 */
@Data
public class CooperativeTripMember {
    private Integer id;
    private Integer tripId;
    private Integer userId;
    private String role; // creator / collaborator
    private Boolean completed;
    private Integer checkedCount;
    private Integer pendingCount;
    private String pendingNames;
}



