package com.example.check.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 模板物品实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateItem {
    private Integer id;
    private Integer templateId;
    private Integer categoryId;
    private Integer itemOverviewId;
    private String name;
    private String note;
    private String priority;
    private Boolean isActive;  // 物品总览的激活状态，从item_overview表关联获取
}







