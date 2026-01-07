package com.example.check.service.serviceImpl;

import com.example.check.mapper.ItemOverviewMapper;
import com.example.check.pojo.ItemOverview;
import com.example.check.service.ItemOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 物品总览服务实现类
 */
@Service
public class ItemOverviewServiceImpl implements ItemOverviewService {
    
    @Autowired
    private ItemOverviewMapper itemOverviewMapper;
    
    @Override
    public List<ItemOverview> getAll() {
        return itemOverviewMapper.getAll();
    }


    @Override
    public List<ItemOverview> AdminGetAll() {
        return itemOverviewMapper.AdminGetAll();
    }

    @Override
    public ItemOverview getById(Integer id) {
        return itemOverviewMapper.getById(id);
    }
    
    @Override
    public List<ItemOverview> getByCategoryId(Integer categoryId) {
        return itemOverviewMapper.getByCategoryId(categoryId);
    }
    
    @Override
    public List<ItemOverview> getByName(String name) {
        return itemOverviewMapper.getByName(name);
    }
    
    @Override
    public boolean add(ItemOverview itemOverview) {
        return itemOverviewMapper.add(itemOverview) > 0;
    }
    
    @Override
    public boolean update(ItemOverview itemOverview) {
        return itemOverviewMapper.update(itemOverview) > 0;
    }
    
    @Override
    public boolean deleteById(Integer id) {
        return itemOverviewMapper.deleteById(id) > 0;
    }
    
    @Override
    public int count() {
        return itemOverviewMapper.count();
    }
    
    @Override
    public int countByCategoryId(Integer categoryId) {
        return itemOverviewMapper.countByCategoryId(categoryId);
    }
    
    @Override
    public List<ItemOverview> getFilteredItems(String categoryCode, String keyword) {
        return itemOverviewMapper.getFilteredItems(categoryCode, keyword);
    }
}


