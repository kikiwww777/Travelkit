package com.example.check.cooperative.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.example.check.cooperative.mapper.CooperativeInviteMapper;
import com.example.check.cooperative.service.CooperativeInviteService;
import com.example.check.cooperative.service.CooperativeTripService;
import com.example.check.cooperative.pojo.CooperativeInvite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CooperativeInviteServiceImpl implements CooperativeInviteService {

    private static final int QR_SIZE = 360;

    @Value("${app.miniProgram.joinPath:/pages/cooperative-join/cooperative-join}")
    private String joinPath;

    @Value("${app.share.baseHost:https://mini-program.example.com}")
    private String baseHost;

    @Autowired
    private CooperativeTripService tripService;

    @Autowired
    private CooperativeInviteMapper inviteMapper;

    @Override
    public CooperativeInvite generateQrInvite(Integer tripId, Integer userId) {
        ensureCreator(tripId, userId);
        
        // 检查是否已存在相同trip_id和invite_type的记录
        CooperativeInvite existingInvite = inviteMapper.findByTripIdAndInviteType(tripId, "QR");
        
        String token = buildToken();
        CooperativeInvite invite;
        
        if (existingInvite != null) {
            // 如果已存在，更新记录
            invite = existingInvite;
            invite.setToken(token);
            invite.setShareLink(buildJoinLink(token));
            invite.setExpiredAt(LocalDateTime.now().plusDays(3));
            inviteMapper.update(invite);
        } else {
            // 如果不存在，新建记录
            invite = buildInvite(tripId, userId, "QR");
            invite.setToken(token);
            invite.setShareLink(buildJoinLink(token));
            inviteMapper.insert(invite);
        }
        
        invite.setShareLink(buildQrBase64(invite.getShareLink()));
        return invite;
    }

    @Override
    public CooperativeInvite generateCodeInvite(Integer tripId, Integer userId, String code) {
        ensureCreator(tripId, userId);
        
        // 检查是否已存在相同trip_id和invite_type的记录
        CooperativeInvite existingInvite = inviteMapper.findByTripIdAndInviteType(tripId, "CODE");
        
        // 如果前端没有传递code，则后端生成
        String inviteCode = (code != null && !code.trim().isEmpty()) ? code.trim().toUpperCase() : buildShortCode();
        
        CooperativeInvite invite;
        
        if (existingInvite != null) {
            // 如果已存在，更新记录
            invite = existingInvite;
            invite.setShortCode(inviteCode);
            invite.setExpiredAt(LocalDateTime.now().plusDays(3));
            inviteMapper.update(invite);
        } else {
            // 如果不存在，新建记录
            invite = buildInvite(tripId, userId, "CODE");
            invite.setShortCode(inviteCode);
            inviteMapper.insert(invite);
        }
        
        return invite;
    }

    @Override
    public CooperativeInvite generateWechatInvite(Integer tripId, Integer userId) {
        ensureCreator(tripId, userId);
        
        // 检查是否已存在相同trip_id和invite_type的记录
        CooperativeInvite existingInvite = inviteMapper.findByTripIdAndInviteType(tripId, "WECHAT");
        
        String token = buildToken();
        CooperativeInvite invite;
        
        if (existingInvite != null) {
            // 如果已存在，更新记录
            invite = existingInvite;
            invite.setToken(token);
            invite.setShareLink(buildJoinLink(token));
            invite.setExpiredAt(LocalDateTime.now().plusDays(3));
            inviteMapper.update(invite);
        } else {
            // 如果不存在，新建记录
            invite = buildInvite(tripId, userId, "WECHAT");
            invite.setToken(token);
            invite.setShareLink(buildJoinLink(token));
            inviteMapper.insert(invite);
        }
        
        return invite;
    }

    @Override
    public void ensureCreator(Integer tripId, Integer userId) {
        if (!tripService.isTripCreator(tripId, userId)) {
            throw new IllegalArgumentException("仅行程创建人可以执行此操作");
        }
    }

    @Override
    public CooperativeInvite findValidInviteByToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        CooperativeInvite invite = inviteMapper.findByToken(token.trim());
        return validateInvite(invite);
    }

    @Override
    public CooperativeInvite findValidInviteByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        CooperativeInvite invite = inviteMapper.findByShortCode(code.trim().toUpperCase());
        return validateInvite(invite);
    }

    private CooperativeInvite buildInvite(Integer tripId, Integer userId, String type) {
        CooperativeInvite invite = new CooperativeInvite();
        invite.setTripId(tripId);
        invite.setInviteType(type);
        invite.setCreatedBy(userId);
        invite.setExpiredAt(LocalDateTime.now().plusDays(3));
        invite.setCreatedAt(LocalDateTime.now());
        return invite;
    }

    private String buildToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String buildShortCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
    }

    private String buildJoinLink(String token) {
        return String.format("%s%s?token=%s", baseHost, joinPath, token);
    }

    private String buildQrBase64(String content) {
        BufferedImage image = QrCodeUtil.generate(content, QR_SIZE, QR_SIZE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImgUtil.write(image, ImgUtil.IMAGE_TYPE_PNG, baos);
        return "data:image/png;base64," + Base64.encode(baos.toByteArray());
    }

    private CooperativeInvite validateInvite(CooperativeInvite invite) {
        if (invite == null) {
            return null;
        }
        if (invite.getExpiredAt() != null && invite.getExpiredAt().isBefore(LocalDateTime.now())) {
            return null;
        }
        return invite;
    }

    @Override
    public boolean deleteByTripId(Integer tripId) {
        return inviteMapper.deleteByTripId(tripId) > 0;
    }
}
