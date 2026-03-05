package com.xuanji.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuanji.entity.KeyTask;

import java.util.List;

/**
 * 重点任务Service接口
 */
public interface KeyTaskService extends IService<KeyTask> {

    /**
     * 分页查询重点任务列表
     * @param page 分页参数
     * @param mainDomain 主要领域
     * @param leadDepartment 牵头部门
     * @param leadPerson 牵头人
     * @return 分页结果
     */
    IPage<KeyTask> getKeyTaskPage(Page<KeyTask> page, String mainDomain, String leadDepartment, String leadPerson);

    /**
     * 根据ID查询重点任务详情
     * @param id 任务ID
     * @return 重点任务详情
     */
    KeyTask getKeyTaskById(Long id);

    /**
     * 创建重点任务
     * @param keyTask 重点任务
     * @return 是否成功
     */
    boolean createKeyTask(KeyTask keyTask);

    /**
     * 更新重点任务
     * @param keyTask 重点任务
     * @return 是否成功
     */
    boolean updateKeyTask(KeyTask keyTask);

    /**
     * 删除重点任务
     * @param id 任务ID
     * @return 是否成功
     */
    boolean deleteKeyTask(Long id);

    /**
     * 查询所有牵头部门
     * @return 牵头部门列表
     */
    List<String> getAllLeadDepartments();

    /**
     * 查询所有牵头人
     * @return 牵头人列表
     */
    List<String> getAllLeadPersons();
}