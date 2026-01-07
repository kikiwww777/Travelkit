package com.example.check.cooperative.mapper;

import com.example.check.cooperative.dto.MemberProgressDTO;
import com.example.check.cooperative.pojo.CooperativeTrip;
import com.example.check.cooperative.pojo.CooperativeTripMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CooperativeTripMapper {

    CooperativeTrip selectById(@Param("id") Integer id);

    List<CooperativeTrip> selectByUserId(@Param("userId") Integer userId);

    List<MemberProgressDTO> selectMemberProgress(@Param("tripId") Integer tripId);

    Integer countPendingItems(@Param("tripId") Integer tripId, @Param("userId") Integer userId);

    int insert(CooperativeTrip trip);

    int updateName(@Param("id") Integer id, @Param("name") String name);

    int updateImg(@Param("id") Integer id, @Param("img") String img);

    void insertItemsFromTemplate(@Param("tripId") Integer tripId,
                                 @Param("templateId") Integer templateId,
                                 @Param("creatorId") Integer creatorId);

    void ensureCreatorMembership(@Param("tripId") Integer tripId,
                                 @Param("userId") Integer userId,
                                 @Param("memberId") Integer memberId);

    void touchProgress(@Param("tripId") Integer tripId);

    int deleteById(@Param("id") Integer id);

    CooperativeTripMember selectMember(@Param("tripId") Integer tripId,
                                       @Param("userId") Integer userId);

    int insertMember(@Param("tripId") Integer tripId,
                     @Param("userId") Integer userId,
                     @Param("role") String role);

    List<CooperativeTripMember> selectMembers(@Param("tripId") Integer tripId);

    int updateMemberAvatar(@Param("tripId") Integer tripId,
                           @Param("userId") Integer userId,
                           @Param("avatarUrl") String avatarUrl);

    int deleteMembersByTripId(@Param("tripId") Integer tripId);

    int updateStatus(@Param("id") Integer id, @Param("status") String status);
}
