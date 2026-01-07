package com.example.check.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 行程实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trip {
    private Integer id;
    private Integer userId;
    private String name;
    private String destination;
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
    private String locationId;
    private String img; // 行程封面图片
}
