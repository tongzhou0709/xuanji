package com.xuanji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuanji.entity.KeyTask;
import com.xuanji.mapper.KeyTaskMapper;
import com.xuanji.service.KeyTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 重点任务Service实现类
 */
@Service
public class KeyTaskServiceImpl extends ServiceImpl<KeyTaskMapper, KeyTask> implements KeyTaskService {

    @Override
    public IPage<KeyTask> getKeyTaskPage(Page<KeyTask> page, String mainDomain, String leadDepartment, String leadPerson) {
        return baseMapper.selectKeyTaskPage(page, mainDomain, leadDepartment, leadPerson);
    }

    @Override
    public KeyTask getKeyTaskById(Long id) {
        return baseMapper.selectKeyTaskById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createKeyTask(KeyTask keyTask) {
        // 设置创建时间和更新时间
        keyTask.setCreateTime(LocalDate.now());
        keyTask.setUpdateTime(LocalDate.now());
        return save(keyTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateKeyTask(KeyTask keyTask) {
        // 设置更新时间
        keyTask.setUpdateTime(LocalDate.now());
        return updateById(keyTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteKeyTask(Long id) {
        // 逻辑删除
        KeyTask keyTask = new KeyTask();
        keyTask.setId(id);
        keyTask.setDeleted(1);
        keyTask.setUpdateTime(LocalDate.now());
        return updateById(keyTask);
    }

    @Override
    public List<String> getAllLeadDepartments() {
        return baseMapper.selectAllLeadDepartments();
    }

    @Override
    public List<String> getAllLeadPersons() {
        return baseMapper.selectAllLeadPersons();
    }
}