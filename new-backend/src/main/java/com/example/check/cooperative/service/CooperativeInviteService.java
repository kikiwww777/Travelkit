package com.example.check.cooperative.service;

import com.example.check.cooperative.pojo.CooperativeInvite;

public interface CooperativeInviteService {

    CooperativeInvite generateQrInvite(Integer tripId, Integer userId);

    CooperativeInvite generateCodeInvite(Integer tripId, Integer userId, String code);

    CooperativeInvite generateWechatInvite(Integer tripId, Integer userId);

    void ensureCreator(Integer tripId, Integer userId);

    CooperativeInvite findValidInviteByToken(String token);

    CooperativeInvite findValidInviteByCode(String code);

    boolean deleteByTripId(Integer tripId);
}
