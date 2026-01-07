package com.example.check.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 协作者实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collaborator {
    private Integer id;
    private Integer shareId;
    private Integer userId;
    private String role;
    private Integer invitedBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinedAt;
}
