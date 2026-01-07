package com.example.check.service.serviceImpl;

import com.example.check.mapper.TripTemplateMapper;
import com.example.check.mapper.TemplateItemMapper;
import com.example.check.pojo.TripTemplate;
import com.example.check.service.TripTemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 行程模板服务实现类
 */
@Service
public class TripTemplateServiceImpl implements TripTemplateService {
    
    @Autowired
    private TripTemplateMapper tripTemplateMapper;
    
    @Autowired
    private TemplateItemMapper templateItemMapper;
    
    @Override
    public TripTemplate getById(Integer id) {
        return tripTemplateMapper.selectById(id);
    }
    
    @Override
    public List<TripTemplate> getPublicTemplates() {
        return tripTemplateMapper.selectPublicTemplates();
    }
    
    @Override
    public List<TripTemplate> getByCreatedBy(Integer createdBy) {
        return tripTemplateMapper.selectByCreatedBy(createdBy);
    }
    
    @Override
    public boolean create(TripTemplate template) {
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());
        if (template.getIsPublic() == null) {
            template.setIsPublic(false);
        }
        // 如果模板是公开的，设置isStatus为0（未审核）
        if (template.getIsPublic() != null && template.getIsPublic()) {
            template.setIsStatus(0);
        } else {
            template.setIsStatus(null);
        }
        return tripTemplateMapper.insert(template) > 0;
    }
    
    @Override
    public boolean update(TripTemplate template) {
        template.setUpdatedAt(LocalDateTime.now());
        return tripTemplateMapper.update(template) > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteById(Integer id) {
        // 先删除模板相关的所有物品（template_items）
        templateItemMapper.deleteByTemplateId(id);
        // 再删除模板本身（trip_templates）
        return tripTemplateMapper.deleteById(id) > 0;
    }
    
    @Override
    public PageInfo<TripTemplate> getPage(Boolean isPublic, String type, String destination, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TripTemplate> templates = tripTemplateMapper.selectByPage(isPublic, type, destination, 0, 0);
        return new PageInfo<>(templates);
    }
    
    @Override
    public List<TripTemplate> getByType(String type) {
        return tripTemplateMapper.selectByType(type);
    }
    
    @Override
    public List<TripTemplate> getByDestination(String destination) {
        return tripTemplateMapper.selectByDestination(destination);
    }
    
    @Override
    public boolean isOwner(Integer templateId, Integer userId) {
        if (templateId == null || userId == null) {
            return false;
        }
        TripTemplate template = tripTemplateMapper.selectById(templateId);
        return template != null && template.getCreatedBy() != null && template.getCreatedBy().equals(userId);
    }
    
    @Override
    public List<TripTemplate> getAllTemplates() {
        return tripTemplateMapper.selectAll();
    }
    
    @Override
    public boolean approveTemplate(Integer id, String result) {
        TripTemplate template = tripTemplateMapper.selectById(id);
        if (template == null) {
            return false;
        }
        template.setIsStatus(1); // 审核通过
        template.setResult(result);
        template.setUpdatedAt(LocalDateTime.now());
        return tripTemplateMapper.update(template) > 0;
    }
    
    @Override
    public boolean rejectTemplate(Integer id, String result) {
        TripTemplate template = tripTemplateMapper.selectById(id);
        if (template == null) {
            return false;
        }
        template.setIsStatus(2); // 审核拒绝
        template.setResult(result);
        template.setUpdatedAt(LocalDateTime.now());
        return tripTemplateMapper.update(template) > 0;
    }
    
}
