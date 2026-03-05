package com.xuanji.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuanji.entity.KeyTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 重点任务Mapper接口
 */
@Mapper
public interface KeyTaskMapper extends BaseMapper<KeyTask> {

    /**
     * 分页查询重点任务列表
     * @param page 分页参数
     * @param mainDomain 主要领域
     * @param leadDepartment 牵头部门
     * @param leadPerson 牵头人
     * @return 分页结果
     */
    IPage<KeyTask> selectKeyTaskPage(Page<KeyTask> page, 
                                    @Param("mainDomain") String mainDomain,
                                    @Param("leadDepartment") String leadDepartment,
                                    @Param("leadPerson") String leadPerson);

    /**
     * 根据ID查询重点任务详情
     * @param id 任务ID
     * @return 重点任务详情
     */
    KeyTask selectKeyTaskById(@Param("id") Long id);

    /**
     * 查询所有牵头部门
     * @return 牵头部门列表
     */
    List<String> selectAllLeadDepartments();

    /**
     * 查询所有牵头人
     * @return 牵头人列表
     */
    List<String> selectAllLeadPersons();
}