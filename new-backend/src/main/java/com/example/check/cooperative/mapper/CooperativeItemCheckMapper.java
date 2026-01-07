package com.example.check.cooperative.mapper;

import com.example.check.cooperative.pojo.CooperativeItemCheck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CooperativeItemCheckMapper {

    /**
     * 查询指定行程中某用户的所有物品查验记录
     */
    List<CooperativeItemCheck> selectByTripIdAndUserId(@Param("tripId") Integer tripId,
                                                       @Param("userId") Integer userId);

    /**
     * 插入或更新用户的查验记录
     */
    int insertOrUpdate(CooperativeItemCheck check);

    /**
     * 删除某个物品的查验记录
     */
    int deleteByItemId(@Param("itemId") Integer itemId);

    /**
     * 删除行程下某个用户的查验记录
     */
    int deleteByTripIdAndUserId(@Param("tripId") Integer tripId,
                                @Param("userId") Integer userId);

    /**
     * 删除行程下所有用户的查验记录
     */
    int deleteByTripId(@Param("tripId") Integer tripId);

    /**
     * 删除指定物品和用户的查验记录
     */
    int deleteByItemIdAndUserId(@Param("itemId") Integer itemId, @Param("userId") Integer userId);
}
