package com.example.check.cooperative.service;

import com.example.check.cooperative.pojo.CooperativeItem;

import java.util.List;

public interface CooperativeItemService {

    List<CooperativeItem> listByTrip(Integer tripId, Integer userId);

    boolean addItem(CooperativeItem item);

    boolean updateChecked(Integer itemId, Integer checked, Integer userId, String scope, Integer tripId);

    boolean deleteItem(Integer itemId);

    boolean resetChecked(Integer tripId, Integer userId);

    boolean deleteByTripId(Integer tripId);

    boolean deleteCheckRecord(Integer itemId, Integer userId);
}


