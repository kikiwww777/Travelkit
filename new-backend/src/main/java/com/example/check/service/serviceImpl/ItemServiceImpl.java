package com.example.check.service.serviceImpl;

import com.example.check.mapper.ItemMapper;
import com.example.check.mapper.ItemOverviewMapper;
import com.example.check.mapper.TemplateItemMapper;
import com.example.check.pojo.Item;
import com.example.check.pojo.ItemOverview;
import com.example.check.pojo.TemplateItem;
import com.example.check.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物品服务实现类
 */
@Service
public class ItemServiceImpl implements ItemService {
    
    @Autowired
    private ItemMapper itemMapper;
    
    @Autowired
    private TemplateItemMapper templateItemMapper;
    
    @Autowired
    private ItemOverviewMapper itemOverviewMapper;
    
    @Override
    public Item getById(Integer id) {
        return itemMapper.selectById(id);
    }
    
    @Override
    public List<Item> getByTripId(Integer tripId) {
        return itemMapper.selectByTripId(tripId);
    }
    
    @Override
    public boolean add(Item item) {
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        if (item.getChecked() == null) {
            item.setChecked(0);
        }
        if (item.getPriority() == null) {
            item.setPriority("medium");
        }
        return itemMapper.insert(item) > 0;
    }
    
    @Override
    public boolean batchAdd(List<Item> items) {
        for (Item item : items) {
            item.setCreatedAt(LocalDateTime.now());
            item.setUpdatedAt(LocalDateTime.now());
            if (item.getChecked() == null) {
                item.setChecked(0);
            }
            if (item.getPriority() == null) {
                item.setPriority("medium");
            }
        }
        return itemMapper.batchInsert(items) > 0;
    }
    
    @Override
    public boolean update(Item item) {
        item.setUpdatedAt(LocalDateTime.now());
        return itemMapper.update(item) > 0;
    }
    
    @Override
    public boolean deleteById(Integer id) {
        return itemMapper.deleteById(id) > 0;
    }
    
    @Override
    public boolean updateCheckedStatus(Integer itemId, Integer checked) {
        return itemMapper.updateCheckedStatus(itemId, checked) > 0;
    }
    
    @Override
    public boolean resetCheckedStatusByTripId(Integer tripId) {
        return itemMapper.resetCheckedStatusByTripId(tripId) > 0;
    }
    
    @Override
    public List<Item> getByCategory(Integer tripId, Integer categoryId) {
        return itemMapper.selectByCategoryId(categoryId).stream()
                .filter(item -> item.getTripId().equals(tripId))
                .toList();
    }
    
    @Override
    public List<Item> getByCheckedStatus(Integer tripId, Integer checked) {
        return itemMapper.selectByCheckedStatus(tripId, checked);
    }
    
    @Override
    public int countByTripId(Integer tripId) {
        return itemMapper.countByTripId(tripId);
    }
    
    @Override
    public int countCheckedByTripId(Integer tripId) {
        return itemMapper.countCheckedByTripId(tripId);
    }
    
    @Override
    @Transactional
    public boolean createFromTemplate(Integer tripId, Integer templateId) {
        try {
            // 查询模板物品列表
            List<TemplateItem> templateItems = templateItemMapper.selectByTemplateId(templateId);
            
            if (templateItems == null || templateItems.isEmpty()) {
                return false;
            }
            
            // 将模板物品转换为物品并批量插入
            List<Item> items = templateItems.stream().map(templateItem -> {
                Item item = new Item();
                item.setTripId(tripId);
                
                // 设置item_overview_id：优先使用模板中的itemOverviewId，如果为null则根据name和categoryId查找
                Integer itemOverviewId = templateItem.getItemOverviewId();
                if (itemOverviewId == null && templateItem.getName() != null && templateItem.getCategoryId() != null) {
                    ItemOverview itemOverview = itemOverviewMapper.getByNameAndCategoryId(
                        templateItem.getName(), 
                        templateItem.getCategoryId()
                    );
                    if (itemOverview != null) {
                        itemOverviewId = itemOverview.getId();
                    }
                }
                item.setItemOverviewId(itemOverviewId);
                
                item.setCategoryId(templateItem.getCategoryId());
                item.setName(templateItem.getName());
                item.setNote(templateItem.getNote());
                item.setPriority(templateItem.getPriority() != null ? templateItem.getPriority() : "medium");
                item.setChecked(0);
                return item;
            }).toList();
            
            return batchAdd(items);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteByItemOverviewId(Integer tripId, Integer itemOverviewId) {
        return itemMapper.deleteByItemOverviewId(tripId, itemOverviewId) > 0;
    }
    
    @Override
    public Item getByItemOverviewId(Integer tripId, Integer itemOverviewId) {
        return itemMapper.selectByItemOverviewId(tripId, itemOverviewId);
    }
    
    @Override
    public List<Item> getFilteredItems(Integer tripId, String categoryCode, Integer checked, String keyword) {
        return itemMapper.selectFilteredItems(tripId, categoryCode, checked, keyword);
    }
    
    @Override
    @Transactional
    public boolean deleteAllCheckedItemsByTripId(Integer tripId) {
        // 删除该行程中所有已勾选的物品（items表中的所有记录）
        // 因为在这个系统中，items表中的记录就代表已勾选的物品
        return itemMapper.deleteByTripId(tripId) > 0;
    }

    @Override
    public boolean deleteByTripId(Integer tripId) {
        return itemMapper.deleteByTripId(tripId) > 0;
    }
}