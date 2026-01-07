package com.example.check.cooperative.mapper;

import com.example.check.cooperative.pojo.CooperativeInvite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CooperativeInviteMapper {

    int insert(CooperativeInvite invite);
    
    int update(CooperativeInvite invite);

    CooperativeInvite findLatestInvite(@Param("tripId") Integer tripId,
                                       @Param("inviteType") String inviteType,
                                       @Param("createdBy") Integer createdBy);
    
    CooperativeInvite findByTripIdAndInviteType(@Param("tripId") Integer tripId,
                                                 @Param("inviteType") String inviteType);

    CooperativeInvite findByToken(@Param("token") String token);

    CooperativeInvite findByShortCode(@Param("shortCode") String shortCode);

    int deleteByTripId(@Param("tripId") Integer tripId);
}
