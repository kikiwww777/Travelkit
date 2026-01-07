package com.example.check.service.serviceImpl;

import com.example.check.mapper.ItemCategoryMapper;
import com.example.check.pojo.ItemCategory;
import com.example.check.service.ItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物品分类服务实现类
 */
@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {
    
    @Autowired
    private ItemCategoryMapper itemCategoryMapper;
    
    @Override
    public ItemCategory getById(Integer id) {
        return itemCategoryMapper.selectById(id);
    }
    
    @Override
    public ItemCategory getByCode(String code) {
        return itemCategoryMapper.selectByCode(code);
    }
    
    @Override
    public List<ItemCategory> getAll() {
        return itemCategoryMapper.selectAll();
    }
    
    @Override
    public boolean add(ItemCategory category) {
        category.setCreatedAt(LocalDateTime.now());
        return itemCategoryMapper.insert(category) > 0;
    }
    
    @Override
    public boolean update(ItemCategory category) {
        return itemCategoryMapper.update(category) > 0;
    }
    
    @Override
    public boolean deleteById(Integer id) {
        return itemCategoryMapper.deleteById(id) > 0;
    }
}
