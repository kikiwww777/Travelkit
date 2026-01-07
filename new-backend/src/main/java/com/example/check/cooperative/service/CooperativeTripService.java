package com.example.check.cooperative.service;

import com.example.check.cooperative.dto.MemberProgressDTO;
import com.example.check.cooperative.pojo.CooperativeTrip;
import com.example.check.cooperative.pojo.CooperativeTripMember;

import java.util.List;

public interface CooperativeTripService {

    CooperativeTrip getById(Integer id, Integer userId);

    List<MemberProgressDTO> listMemberProgress(Integer tripId, String statusFilter);

    boolean isTripCreator(Integer tripId, Integer userId);

    List<CooperativeTrip> listByUser(Integer userId);

    boolean updateStatus(Integer tripId, String status);

    boolean createTrip(CooperativeTrip trip, Integer templateId);

    boolean updateTripName(Integer tripId, String name);

    boolean updateTripImg(Integer tripId, String img);

    boolean deleteTrip(Integer tripId);

    boolean addCollaborator(Integer tripId, Integer userId);

    boolean isTripMember(Integer tripId, Integer userId);

    List<CooperativeTripMember> listMembers(Integer tripId);

    /**
     * 根据成员查验进度刷新行程整体完成状态。
     * @param tripId 行程ID
     * @return true 表示所有成员均已完成并已标记为 completed；false 表示仍有成员未完成
     */
    boolean refreshCompletionStatus(Integer tripId);

    boolean updateMemberAvatar(Integer tripId, Integer userId, String avatarUrl);
}

