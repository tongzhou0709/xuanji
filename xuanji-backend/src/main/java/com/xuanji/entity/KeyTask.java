package com.xuanji.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 重点任务实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("key_task")
public class KeyTask {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 主要领域
     */
    @TableField("main_domain")
    private String mainDomain;

    /**
     * 重点任务名称
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 具体举措
     */
    @TableField("specific_measures")
    private String specificMeasures;

    /**
     * 预期实现效果
     */
    @TableField("expected_effect")
    private String expectedEffect;

    /**
     * 关键词1
     */
    @TableField("keyword1")
    private String keyword1;

    /**
     * 关键词2
     */
    @TableField("keyword2")
    private String keyword2;

    /**
     * 关键词3
     */
    @TableField("keyword3")
    private String keyword3;

    /**
     * 标志成果或交付物
     */
    @TableField("deliverables")
    private String deliverables;

    /**
     * 牵头部门
     */
    @TableField("lead_department")
    private String leadDepartment;

    /**
     * 牵头人
     */
    @TableField("lead_person")
    private String leadPerson;

    /**
     * 计划开始时间
     */
    @TableField("planned_start_date")
    private LocalDate plannedStartDate;

    /**
     * 计划完成时间
     */
    @TableField("planned_end_date")
    private LocalDate plannedEndDate;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDate createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDate updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 逻辑删除标记
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}