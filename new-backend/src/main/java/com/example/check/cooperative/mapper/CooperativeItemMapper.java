package com.example.check.cooperative.mapper;

import com.example.check.cooperative.pojo.CooperativeItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CooperativeItemMapper {

    List<CooperativeItem> selectByTripId(@Param("tripId") Integer tripId);

    CooperativeItem selectById(@Param("id") Integer id);
    
    CooperativeItem selectByTripIdAndName(@Param("tripId") Integer tripId, @Param("name") String name);
    
    CooperativeItem selectByTripIdAndItemOverviewId(@Param("tripId") Integer tripId, @Param("itemOverviewId") Integer itemOverviewId);

    int insert(CooperativeItem item);

    int deleteById(@Param("id") Integer id);

    int deleteByTripId(@Param("tripId") Integer tripId);
}


