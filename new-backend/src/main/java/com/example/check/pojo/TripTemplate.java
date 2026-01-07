package com.example.check.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 行程模板实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripTemplate {
    private Integer id;
    private String name;
    private String description;
    private String destination;
    private Integer duration;
    private String type;
    private String tags;
    private Boolean isPublic;
    private Integer isStatus; // 审核状态：0-未审核，1-审核通过，2-未通过
    private String result; // 审核原因
    private Integer createdBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
