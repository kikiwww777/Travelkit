package com.example.check.cooperative.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合作行程实体，与个人行程字段保持一致，但数据来源于 cooperative_* 表。
 */
@Data
public class CooperativeTrip {
    private Integer id;
    private Integer creatorId;
    private String name;
    private String destination;
    private String locationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer duration;
    private Integer travelers;
    private String type;
    private String budget;
    private String description;
    private String specialNeeds;
    private String status;
    private Integer checkedItems;
    private Integer totalItems;
    private Integer progress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private String img; // 行程封面图片
}



