package com.example.check.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 物品实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Integer id;
    private Integer tripId;
    private Integer itemOverviewId;
    private Integer categoryId;
    private String name;
    private String note;
    private String priority;
    private Integer checked; // 0=未查验, 1=已查验, 2=已跳过
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
