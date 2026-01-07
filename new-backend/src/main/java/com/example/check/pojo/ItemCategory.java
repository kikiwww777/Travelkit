package com.example.check.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 物品分类实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCategory {
    private Integer id;
    private String name;
    private String code;
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
