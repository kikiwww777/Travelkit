package com.example.check.service.serviceImpl;

import com.example.check.mapper.TripMapper;
import com.example.check.pojo.Trip;
import com.example.check.service.TripService;
import com.example.check.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 行程服务实现类
 */
@Service
public class TripServiceImpl implements TripService {
    
    @Autowired
    private TripMapper tripMapper;

    @Autowired
    private ItemService itemService;
    
    @Override
    public Trip getById(Integer id) {
        return tripMapper.selectById(id);
    }
    
    @Override
    public List<Trip> getByUserId(Integer userId) {
        return tripMapper.selectByUserId(userId);
    }
    
    @Override
    public boolean create(Trip trip) {
        trip.setCreatedAt(LocalDateTime.now());
        trip.setUpdatedAt(LocalDateTime.now());
        trip.setCheckedItems(0);
        trip.setTotalItems(0);
        trip.setProgress(0);
        if (trip.getName() == null || trip.getName().trim().isEmpty()) {
            trip.setName(trip.getDestination());
        }
        if (trip.getStatus() == null) {
            trip.setStatus("preparing");
        }
        return tripMapper.insert(trip) > 0;
    }
    
    @Override
    public boolean update(Trip trip) {
        trip.setUpdatedAt(LocalDateTime.now());
        return tripMapper.update(trip) > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteById(Integer id) {
        // 级联删除相关数据
        // 1. 先删除items表中与trip_id对应的物品
        itemService.deleteByTripId(id);
        // 2. 然后删除trips表中的行程数据
        return tripMapper.deleteById(id) > 0;
    }
    
    @Override
    public PageInfo<Trip> getPage(Integer userId, String status, String destination, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Trip> trips = tripMapper.selectByPage(userId, status, destination, 0, 0);
        return new PageInfo<>(trips);
    }
    
    @Override
    public boolean updateProgress(Integer tripId, Integer checkedItems, Integer totalItems, Integer progress) {
        return tripMapper.updateProgress(tripId, checkedItems, totalItems, progress) > 0;
    }
    
    @Override
    public List<Trip> getByStatus(String status) {
        return tripMapper.selectByStatus(status);
    }
    
    @Override
    public List<Trip> getByDestination(String destination) {
        return tripMapper.selectByDestination(destination);
    }
    
    @Override
    public List<Trip> getRecentTrips(Integer userId, int limit) {
        List<Trip> trips = tripMapper.selectByUserId(userId);
        return trips.stream().limit(limit).toList();
    }
    
    @Override
    public boolean updateStatus(Integer tripId, String status) {
        return tripMapper.updateStatus(tripId, status) > 0;
    }

    @Override
    public boolean updateName(Integer tripId, String name) {
        return tripMapper.updateName(tripId, name) > 0;
    }

    @Override
    public boolean updateImg(Integer tripId, String img) {
        return tripMapper.updateImg(tripId, img) > 0;
    }
}