package com.example.check.cooperative.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CooperativeItemCheck {
    private Integer id;
    private Integer itemId;
    private Integer tripId; // 行程ID，关联cooperative_trips表
    private Integer userId;
    private Integer checked; // 0=未查验, 1=已携带, 2=已跳过
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}


