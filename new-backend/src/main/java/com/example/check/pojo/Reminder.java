package com.example.check.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 智能提醒实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reminder {
    private Integer id;
    private Integer tripId;
    private String type;
    private String title;
    private String message;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime triggerTime;
    
    private Boolean isSent;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
