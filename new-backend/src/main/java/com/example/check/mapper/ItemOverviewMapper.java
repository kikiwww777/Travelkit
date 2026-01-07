package com.example.check.mapper;

import com.example.check.pojo.ItemOverview;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 物品总览Mapper接口
 */
@Mapper
public interface ItemOverviewMapper {
    
    /**
     * 查询所有物品总览
     */
    @Select("SELECT id, name, category_id, description, image_url, " +
            "tags, is_active, created_at, updated_at " +
            "FROM item_overview WHERE is_active = 1 ORDER BY created_at DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "categoryId", column = "category_id"),
        @Result(property = "description", column = "description"),
        @Result(property = "imageUrl", column = "image_url"),
        @Result(property = "tags", column = "tags", typeHandler = com.example.check.config.JsonTypeHandler.class),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<ItemOverview> getAll();

    @Select("select * from `check`.item_overview")
    List<ItemOverview> AdminGetAll();
    
    /**
     * 根据ID查询物品总览
     */
    @Select("SELECT id, name, category_id, description, image_url, " +
            "tags, is_active, created_at, updated_at " +
            "FROM item_overview WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "categoryId", column = "category_id"),
        @Result(property = "description", column = "description"),
        @Result(property = "imageUrl", column = "image_url"),
        @Result(property = "tags", column = "tags", typeHandler = com.example.check.config.JsonTypeHandler.class),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    ItemOverview getById(@Param("id") Integer id);
    
    /**
     * 根据分类ID查询物品总览
     */
    @Select("SELECT id, name, category_id, description, image_url, " +
            "tags, is_active, created_at, updated_at " +
            "FROM item_overview WHERE category_id = #{categoryId} AND is_active = 1 ORDER BY created_at DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "categoryId", column = "category_id"),
        @Result(property = "description", column = "description"),
        @Result(property = "imageUrl", column = "image_url"),
        @Result(property = "tags", column = "tags", typeHandler = com.example.check.config.JsonTypeHandler.class),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<ItemOverview> getByCategoryId(@Param("categoryId") Integer categoryId);
    
    /**
     * 根据名称模糊查询物品总览
     */
    @Select("SELECT id, name, category_id, description, image_url, " +
            "tags, is_active, created_at, updated_at " +
            "FROM item_overview WHERE name LIKE CONCAT('%', #{name}, '%') AND is_active = 1 ORDER BY created_at DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "categoryId", column = "category_id"),
        @Result(property = "description", column = "description"),
        @Result(property = "imageUrl", column = "image_url"),
        @Result(property = "tags", column = "tags", typeHandler = com.example.check.config.JsonTypeHandler.class),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<ItemOverview> getByName(@Param("name") String name);
    
    /**
     * 根据名称和分类ID精确查询物品总览
     */
    @Select("SELECT id, name, category_id, description, image_url, " +
            "tags, is_active, created_at, updated_at " +
            "FROM item_overview WHERE name = #{name} AND category_id = #{categoryId} AND is_active = 1 LIMIT 1")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "categoryId", column = "category_id"),
        @Result(property = "description", column = "description"),
        @Result(property = "imageUrl", column = "image_url"),
        @Result(property = "tags", column = "tags", typeHandler = com.example.check.config.JsonTypeHandler.class),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    ItemOverview getByNameAndCategoryId(@Param("name") String name, @Param("categoryId") Integer categoryId);
    
    /**
     * 添加物品总览
     */
    @Insert("INSERT INTO item_overview (name, category_id, description, image_url, tags, is_active, created_at, updated_at) " +
            "VALUES (#{name}, #{categoryId}, #{description}, #{imageUrl}, #{tags, typeHandler=com.example.check.config.JsonTypeHandler}, #{isActive}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int add(ItemOverview itemOverview);
    
    /**
     * 更新物品总览
     */
    @Update("UPDATE item_overview SET name = #{name}, category_id = #{categoryId}, description = #{description}, " +
            "image_url = #{imageUrl}, tags = #{tags, typeHandler=com.example.check.config.JsonTypeHandler}, " +
            "is_active = #{isActive}, updated_at = NOW() WHERE id = #{id}")
    int update(ItemOverview itemOverview);
    
    /**
     * 删除物品总览（软删除）
     */
    @Update("UPDATE item_overview SET is_active = 0, updated_at = NOW() WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);
    
    /**
     * 统计物品总览数量
     */
    @Select("SELECT COUNT(*) FROM item_overview WHERE is_active = 1")
    int count();
    
    /**
     * 根据分类统计物品总览数量
     */
    @Select("SELECT COUNT(*) FROM item_overview WHERE category_id = #{categoryId} AND is_active = 1")
    int countByCategoryId(@Param("categoryId") Integer categoryId);
    
    /**
     * 根据条件筛选物品总览（支持分类、关键词筛选）
     */
    @Select("<script>" +
            "SELECT io.id, io.name, io.category_id, io.description, io.image_url, " +
            "io.tags, io.is_active, io.created_at, io.updated_at " +
            "FROM item_overview io " +
            "LEFT JOIN item_categories ic ON io.category_id = ic.id " +
            "WHERE io.is_active = 1 " +
            "<if test='categoryCode != null and categoryCode != \"\"'>" +
            "AND ic.code = #{categoryCode} " +
            "</if>" +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (io.name LIKE CONCAT('%', #{keyword}, '%') OR io.description LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "ORDER BY io.created_at DESC" +
            "</script>")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "categoryId", column = "category_id"),
        @Result(property = "description", column = "description"),
        @Result(property = "imageUrl", column = "image_url"),
        @Result(property = "tags", column = "tags", typeHandler = com.example.check.config.JsonTypeHandler.class),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<ItemOverview> getFilteredItems(@Param("categoryCode") String categoryCode, @Param("keyword") String keyword);
}


