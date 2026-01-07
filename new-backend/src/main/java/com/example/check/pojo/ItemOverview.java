package com.example.check.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物品总览实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemOverview {
    private Integer id;
    private String name;
    private Integer categoryId;
    private String description;
    private String imageUrl;
    private List<String> tags;
    private Boolean isActive;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}


