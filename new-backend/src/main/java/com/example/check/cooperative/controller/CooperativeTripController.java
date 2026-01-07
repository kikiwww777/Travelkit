package com.example.check.cooperative.controller;

import com.example.check.cooperative.dto.MemberProgressDTO;
import com.example.check.cooperative.pojo.CooperativeInvite;
import com.example.check.cooperative.pojo.CooperativeTrip;
import com.example.check.cooperative.service.CooperativeInviteService;
import com.example.check.cooperative.service.CooperativeTripService;
import com.example.check.util.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cooperative/trips")
@CrossOrigin(origins = "*")
public class CooperativeTripController {

    @Autowired
    private CooperativeTripService tripService;

    @Autowired
    private CooperativeInviteService inviteService;

    @GetMapping("/{id}")
    public Map<String, Object> getTripDetail(@PathVariable Integer id,
                                             @RequestParam(required = false) String memberStatus,
                                             @RequestParam(required = false) Integer userId) {
        Map<String, Object> result = new HashMap<>();
        CooperativeTrip trip = tripService.getById(id, userId);
        if (trip == null) {
            result.put("success", false);
            result.put("message", "合作行程不存在");
            return result;
        }
        List<MemberProgressDTO> memberProgress = tripService.listMemberProgress(id, memberStatus);
        result.put("success", true);
        result.put("trip", trip);
        result.put("memberProgress", memberProgress);
        return result;
    }

    @GetMapping("/user/{userId}")
    public Map<String, Object> listTrips(@PathVariable Integer userId) {
        List<CooperativeTrip> trips = tripService.listByUser(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", trips);
        return result;
    }

    @PutMapping("/{id}/status")
    public Map<String, Object> updateStatus(@PathVariable Integer id,
                                            @RequestParam String status,
                                            @RequestParam(required = false) Integer userId) {
        Map<String, Object> result = new HashMap<>();
        if (!isValidStatus(status)) {
            result.put("success", false);
            result.put("message", "无效的状态值，支持：preparing、ongoing、completed、cancelled");
            return result;
        }
        if (userId != null && !tripService.isTripMember(id, userId)) {
            result.put("success", false);
            result.put("message", "仅行程成员可更新状态");
            return result;
        }
        boolean success = tripService.updateStatus(id, status);
        result.put("success", success);
        result.put("message", success ? "状态更新成功" : "状态更新失败");
        return result;
    }

    @PostMapping("/{id}/complete")
    public Map<String, Object> completeTrip(@PathVariable Integer id,
                                            @RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        if (userId == null) {
            result.put("success", false);
            result.put("message", "userId 不能为空");
            return result;
        }
        if (!tripService.isTripMember(id, userId)) {
            result.put("success", false);
            result.put("message", "仅行程成员可同步完成状态");
            return result;
        }
        CooperativeTrip trip = tripService.getById(id, userId);
        if (trip == null) {
            result.put("success", false);
            result.put("message", "合作行程不存在");
            return result;
        }
        String status = trip.getStatus() != null ? trip.getStatus().toLowerCase() : "";
        if ("cancelled".equals(status)) {
            result.put("success", false);
            result.put("message", "行程已取消，无法同步完成状态");
            return result;
        }
        boolean tripCompleted = tripService.refreshCompletionStatus(id);
        CooperativeTrip refreshedTrip = tripService.getById(id, userId);
        result.put("success", true);
        result.put("tripCompleted", tripCompleted);
        if (refreshedTrip != null) {
            result.put("trip", refreshedTrip);
            result.put("status", refreshedTrip.getStatus());
        }
        result.put("message", tripCompleted ? "所有成员均已完成" : "已同步您的完成状态");
        return result;
    }

    @PostMapping
    public Map<String, Object> createTrip(@RequestBody Map<String, Object> payload) {
        Map<String, Object> result = new HashMap<>();
        try {
            CooperativeTrip trip = new CooperativeTrip();
            trip.setCreatorId(extractInteger(payload.get("creatorId")));
            String name = (String) payload.get("name");
            String destination = (String) payload.get("destination");
            String description = (String) payload.get("description");
            String specialNeeds = (String) payload.get("specialNeeds");
            // 敏感词检测
            if (name != null && SensitiveWordFilter.containsSensitiveWord(name)) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (destination != null && SensitiveWordFilter.containsSensitiveWord(destination)) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (description != null && SensitiveWordFilter.containsSensitiveWord(description)) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            if (specialNeeds != null && SensitiveWordFilter.containsSensitiveWord(specialNeeds)) {
                result.put("success", false);
                result.put("message", "请勿输入敏感内容");
                return result;
            }
            trip.setName(name);
            trip.setDestination(destination);
            trip.setLocationId(payload.get("locationId") != null ? String.valueOf(payload.get("locationId")) : null);
            trip.setStartDate(payload.get("startDate") != null ? java.time.LocalDate.parse((String) payload.get("startDate")) : null);
            trip.setEndDate(payload.get("endDate") != null ? java.time.LocalDate.parse((String) payload.get("endDate")) : null);
            trip.setDuration(extractInteger(payload.get("duration")));
            trip.setTravelers(extractInteger(payload.get("travelers")));
            trip.setType((String) payload.get("type"));
            trip.setBudget((String) payload.get("budget"));
            trip.setDescription(description);
            trip.setSpecialNeeds(specialNeeds);
            // 行程封面图片
            String img = (String) payload.get("img");
            if (img != null && !img.trim().isEmpty()) {
                trip.setImg(img.trim());
            }
            Integer templateId = extractInteger(payload.get("templateId"));
            boolean created = tripService.createTrip(trip, templateId);
            if (created) {
                result.put("success", true);
                result.put("data", trip);
            } else {
                result.put("success", false);
                result.put("message", "创建合作行程失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建失败: " + e.getMessage());
        }
        return result;
    }

    @PatchMapping("/{id}/name")
    public Map<String, Object> renameTrip(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        Map<String, Object> result = new HashMap<>();
        String name = payload.get("name");
        if (name == null || name.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "名称不能为空");
            return result;
        }
        if (SensitiveWordFilter.containsSensitiveWord(name)) {
            result.put("success", false);
            result.put("message", "请勿输入敏感内容");
            return result;
        }
        boolean ok = tripService.updateTripName(id, name.trim());
        result.put("success", ok);
        result.put("message", ok ? "名称更新成功" : "名称更新失败");
        return result;
    }

    @PatchMapping("/{id}/img")
    public Map<String, Object> updateTripImg(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        Map<String, Object> result = new HashMap<>();
        String img = payload.get("img");
        if (img == null) {
            result.put("success", false);
            result.put("message", "图片URL不能为空");
            return result;
        }
        boolean ok = tripService.updateTripImg(id, img.trim());
        result.put("success", ok);
        result.put("message", ok ? "封面更新成功" : "封面更新失败");
        return result;
    }

    @GetMapping("/{id}/members")
    public Map<String, Object> getMemberProgress(@PathVariable Integer id,
                                                 @RequestParam(required = false) String status) {
        List<MemberProgressDTO> memberProgress = tripService.listMemberProgress(id, status);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", memberProgress);
//
        return result;
    }

    @GetMapping("/{id}/members/basic")
    public Map<String, Object> listMembers(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", tripService.listMembers(id));
        return result;
    }

    @PatchMapping("/{id}/members/avatar")
    public Map<String, Object> updateMemberAvatar(@PathVariable Integer id,
                                                  @RequestBody Map<String, Object> payload) {
        Map<String, Object> result = new HashMap<>();
        Integer userId = extractInteger(payload.get("userId"));
        String avatarUrl = payload.get("avatarUrl") != null ? payload.get("avatarUrl").toString() : null;
        if (userId == null) {
            result.put("success", false);
            result.put("message", "userId 不能为空");
            return result;
        }
        if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "avatarUrl 不能为空");
            return result;
        }
        boolean updated = tripService.updateMemberAvatar(id, userId, avatarUrl.trim());
        result.put("success", updated);
        result.put("message", updated ? "头像更新成功" : "头像更新失败");
        return result;
    }

    @GetMapping("/join/preview")
    public Map<String, Object> previewInvite(@RequestParam(required = false) String token,
                                             @RequestParam(required = false) String code) {
        Map<String, Object> result = new HashMap<>();
        CooperativeInvite invite = resolveInvite(token, code);
        if (invite == null) {
            result.put("success", false);
            result.put("message", "邀请无效或已过期");
            return result;
        }
        CooperativeTrip trip = tripService.getById(invite.getTripId(), null);
        if (trip == null) {
            result.put("success", false);
            result.put("message", "对应的合作行程不存在");
            return result;
        }
        result.put("success", true);
        result.put("trip", trip);
        result.put("inviteType", invite.getInviteType());
        result.put("expiresAt", invite.getExpiredAt());
        return result;
    }

    @PostMapping("/join/apply")
    public Map<String, Object> applyJoin(@RequestBody Map<String, Object> payload) {
        Map<String, Object> result = new HashMap<>();
        Integer userId = extractInteger(payload.get("userId"));
        if (userId == null) {
            result.put("success", false);
            result.put("message", "请先登录");
            return result;
        }
        String token = payload.get("token") != null ? payload.get("token").toString() : null;
        String code = payload.get("code") != null ? payload.get("code").toString() : null;
        CooperativeInvite invite = resolveInvite(token, code);
        if (invite == null) {
            result.put("success", false);
            result.put("message", "邀请无效或已过期");
            return result;
        }
        boolean joined = tripService.addCollaborator(invite.getTripId(), userId);
        result.put("success", joined);
        result.put("tripId", invite.getTripId());
        result.put("message", joined ? "加入成功" : "加入失败");
        return result;
    }

    @PostMapping("/{id}/invite/qrcode")
    public Map<String, Object> generateInviteQr(@PathVariable Integer id,
                                                @RequestParam Integer userId) {
        CooperativeInvite invite = inviteService.generateQrInvite(id, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("qrImage", invite.getShareLink());
        result.put("token", invite.getToken());
        return result;
    }

    @PostMapping("/{id}/invite/code")
    public Map<String, Object> generateInviteCode(@PathVariable Integer id,
                                                  @RequestParam Integer userId,
                                                  @RequestBody(required = false) Map<String, String> payload) {
        Map<String, Object> result = new HashMap<>();
        String code = null;
        if (payload != null && payload.containsKey("code")) {
            code = payload.get("code");
        }
        CooperativeInvite invite = inviteService.generateCodeInvite(id, userId, code);
        result.put("success", true);
        result.put("code", invite.getShortCode());
        return result;
    }

    @PostMapping("/{id}/invite/wechat")
    public Map<String, Object> generateWechatInvite(@PathVariable Integer id,
                                                    @RequestParam Integer userId) {
        CooperativeInvite invite = inviteService.generateWechatInvite(id, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("link", invite.getShareLink());
        result.put("token", invite.getToken());
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteTrip(@PathVariable Integer id,
                                          @RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        if (userId == null || !tripService.isTripCreator(id, userId)) {
            result.put("success", false);
            result.put("message", "仅创建人可删除合作行程");
            return result;
        }
        boolean success = tripService.deleteTrip(id);
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
    private CooperativeInvite resolveInvite(String token, String code) {
        CooperativeInvite invite = null;
        if (token != null && !token.trim().isEmpty()) {
            invite = inviteService.findValidInviteByToken(token.trim());
        }
        if (invite == null && code != null && !code.trim().isEmpty()) {
            invite = inviteService.findValidInviteByCode(code.trim());
        }
        return invite;
    }

    private boolean isValidStatus(String status) {
        if (status == null) {
            return false;
        }
        switch (status.toLowerCase()) {
            case "preparing":
            case "ongoing":
            case "completed":
            case "cancelled":
                return true;
            default:
                return false;
        }
    }

    private Integer extractInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }
}

