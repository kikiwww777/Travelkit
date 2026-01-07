package com.example.check.cooperative.service.impl;

import com.example.check.cooperative.mapper.CooperativeItemCheckMapper;
import com.example.check.cooperative.mapper.CooperativeItemMapper;
import com.example.check.cooperative.pojo.CooperativeItem;
import com.example.check.cooperative.pojo.CooperativeItemCheck;
import com.example.check.cooperative.service.CooperativeItemService;
import com.example.check.mapper.ItemOverviewMapper;
import com.example.check.pojo.ItemOverview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CooperativeItemServiceImpl implements CooperativeItemService {

    @Autowired
    private CooperativeItemMapper itemMapper;
    
    @Autowired
    private CooperativeItemCheckMapper checkMapper;
    
    @Autowired
    private ItemOverviewMapper itemOverviewMapper;

    @Override
    public List<CooperativeItem> listByTrip(Integer tripId, Integer userId) {
        // 查询物品列表
        List<CooperativeItem> items = itemMapper.selectByTripId(tripId);
        
        // 如果有userId，从cooperative_item_checks表查询该用户的查验状态并设置到items中
        if (userId != null) {
            try {
                List<CooperativeItemCheck> checks = checkMapper.selectByTripIdAndUserId(tripId, userId);
                Map<Integer, Integer> checkMap = checks.stream()
                    .collect(Collectors.toMap(CooperativeItemCheck::getItemId, CooperativeItemCheck::getChecked));
                
                // 为每个物品设置当前用户的查验状态（从cooperative_item_checks表查询）
                // checkedStatus: 0=未查验, 1=已携带, 2=已跳过
                // checked: true表示已携带，false表示未查验或已跳过
                for (CooperativeItem item : items) {
                    Integer checkedValue = checkMap.get(item.getId());
                    if (checkedValue != null) {
                        item.setCheckedStatus(checkedValue);
                        item.setChecked(checkedValue == 1); // 只有checked=1时，checked为true
                    } else {
                        item.setCheckedStatus(0);
                        item.setChecked(false);
                    }
                }
            } catch (Exception e) {
                // 如果查询查验记录失败，所有物品的checked状态设为false
                System.err.println("查询用户查验状态失败: " + e.getMessage());
                for (CooperativeItem item : items) {
                    item.setChecked(false);
                }
            }
        } else {
            // 如果没有userId，所有物品的checked状态设为false
            for (CooperativeItem item : items) {
                item.setChecked(false);
            }
        }
        
        return items;
    }

    @Override
    public boolean addItem(CooperativeItem item) {
        // 检查是否已存在相同 trip_id 和 item_overview_id 的物品（避免重复添加相同的item_overview）
        // 注意：不同的item_overview_id即使名称相同也应该可以添加
        if (item.getTripId() != null && item.getItemOverviewId() != null) {
            CooperativeItem existingItem = itemMapper.selectByTripIdAndItemOverviewId(item.getTripId(), item.getItemOverviewId());
            if (existingItem != null) {
                // 如果已存在相同的item_overview_id，直接返回成功（不插入新记录）
                // 将已存在物品的ID设置到item中，以便前端使用
                item.setId(existingItem.getId());
                return true;
            }
        }
        
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        
        // 如果itemOverviewId不为null，从item_overview获取description
        if (item.getItemOverviewId() != null) {
            try {
                ItemOverview itemOverview = itemOverviewMapper.getById(item.getItemOverviewId());
                if (itemOverview != null && itemOverview.getDescription() != null) {
                    // 如果item_overview中有description，则使用它；否则使用item中已有的description
                    if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
                        item.setDescription(itemOverview.getDescription());
                    }
                }
            } catch (Exception e) {
                // 如果查找失败，继续执行，使用item中已有的description
                System.err.println("查找itemOverview失败: " + e.getMessage());
            }
        } else if (item.getItemOverviewId() == null && item.getName() != null && item.getCategoryId() != null) {
            // 如果itemOverviewId为null，尝试根据name和categoryId查找itemOverview
            try {
                ItemOverview itemOverview = itemOverviewMapper.getByNameAndCategoryId(
                    item.getName(), 
                    item.getCategoryId()
                );
                if (itemOverview != null) {
                    item.setItemOverviewId(itemOverview.getId());
                    // 从item_overview获取description
                    if (itemOverview.getDescription() != null) {
                        if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
                            item.setDescription(itemOverview.getDescription());
                        }
                    }
                }
            } catch (Exception e) {
                // 如果查找失败，继续执行，itemOverviewId保持为null
                System.err.println("查找itemOverview失败: " + e.getMessage());
            }
        }
        
        return itemMapper.insert(item) > 0;
    }

    @Override
    @Transactional
    public boolean updateChecked(Integer itemId, Integer checked, Integer userId, String scope, Integer tripId) {
        try {
            // 如果tripId为null，尝试从itemId获取
            Integer actualTripId = tripId;
            if (actualTripId == null) {
                CooperativeItem item = itemMapper.selectById(itemId);
                if (item != null) {
                    actualTripId = item.getTripId();
                }
            }
            
            if (actualTripId == null) {
                System.err.println("无法获取tripId，更新失败");
                return false;
            }
            
            if ("all".equals(scope)) {
                // 如果是"给所有人都添加"，需要获取该物品的所有相关用户
                // 这里简化处理：获取该物品所在行程的所有成员
                // 实际应该查询 cooperative_trip_members 表
                // 暂时只更新当前用户
                CooperativeItemCheck check = new CooperativeItemCheck();
                check.setItemId(itemId);
                check.setTripId(actualTripId);
                check.setUserId(userId);
                check.setChecked(checked != null ? checked : 0);
                return checkMapper.insertOrUpdate(check) > 0;
            } else {
                // "仅给我自己添加"，只更新当前用户的查验状态
                CooperativeItemCheck check = new CooperativeItemCheck();
                check.setItemId(itemId);
                check.setTripId(actualTripId);
                check.setUserId(userId);
                check.setChecked(checked != null ? checked : 0);
                return checkMapper.insertOrUpdate(check) > 0;
            }
        } catch (Exception e) {
            // 如果表不存在，返回失败
            System.err.println("更新用户查验状态失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteItem(Integer itemId) {
        // 删除物品时，同时删除相关的查验记录
        checkMapper.deleteByItemId(itemId);
        return itemMapper.deleteById(itemId) > 0;
    }

    @Override
    public boolean resetChecked(Integer tripId, Integer userId) {
        if (userId != null) {
            try {
                // 重置指定用户的查验状态
                return checkMapper.deleteByTripIdAndUserId(tripId, userId) > 0;
            } catch (Exception e) {
                // 如果表不存在，返回失败
                System.err.println("重置用户查验状态失败: " + e.getMessage());
                return false;
            }
        } else {
            // 重置所有用户的查验状态：删除该行程所有物品的所有查验记录
            try {
                return checkMapper.deleteByTripId(tripId) > 0;
            } catch (Exception e) {
                System.err.println("重置所有用户查验状态失败: " + e.getMessage());
                return false;
            }
        }
    }

    @Override
    public boolean deleteByTripId(Integer tripId) {
        // 删除行程时，相关的查验记录会通过外键级联删除
        return itemMapper.deleteByTripId(tripId) > 0;
    }

    @Override
    public boolean deleteCheckRecord(Integer itemId, Integer userId) {
        return checkMapper.deleteByItemIdAndUserId(itemId, userId) > 0;
    }
}


