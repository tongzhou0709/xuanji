package com.xuanji.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuanji.common.Result;
import com.xuanji.entity.KeyTask;
import com.xuanji.service.KeyTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 重点任务管理Controller
 */
@RestController
@RequestMapping("/keyTask")
public class KeyTaskController {

    @Autowired
    private KeyTaskService keyTaskService;

    /**
     * 分页查询重点任务列表
     */
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('keyTask:list')")
    public Result<IPage<KeyTask>> getKeyTaskPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String mainDomain,
            @RequestParam(required = false) String leadDepartment,
            @RequestParam(required = false) String leadPerson) {
        
        Page<KeyTask> page = new Page<>(current, size);
        IPage<KeyTask> result = keyTaskService.getKeyTaskPage(page, mainDomain, leadDepartment, leadPerson);
        return Result.success(result);
    }

    /**
     * 根据ID查询重点任务详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('keyTask:detail')")
    public Result<KeyTask> getKeyTaskById(@PathVariable Long id) {
        KeyTask keyTask = keyTaskService.getKeyTaskById(id);
        if (keyTask == null) {
            return Result.error("重点任务不存在");
        }
        return Result.success(keyTask);
    }

    /**
     * 创建重点任务
     */
    @PostMapping
    @PreAuthorize("hasAuthority('keyTask:create')")
    public Result<String> createKeyTask(@RequestBody KeyTask keyTask) {
        boolean success = keyTaskService.createKeyTask(keyTask);
        return success ? Result.success("创建成功") : Result.error("创建失败");
    }

    /**
     * 更新重点任务
     */
    @PutMapping
    @PreAuthorize("hasAuthority('keyTask:update')")
    public Result<String> updateKeyTask(@RequestBody KeyTask keyTask) {
        boolean success = keyTaskService.updateKeyTask(keyTask);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 删除重点任务
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('keyTask:delete')")
    public Result<String> deleteKeyTask(@PathVariable Long id) {
        boolean success = keyTaskService.deleteKeyTask(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 获取所有牵头部门
     */
    @GetMapping("/departments")
    @PreAuthorize("hasAuthority('keyTask:list')")
    public Result<List<String>> getAllLeadDepartments() {
        List<String> departments = keyTaskService.getAllLeadDepartments();
        return Result.success(departments);
    }

    /**
     * 获取所有牵头人
     */
    @GetMapping("/persons")
    @PreAuthorize("hasAuthority('keyTask:list')")
    public Result<List<String>> getAllLeadPersons() {
        List<String> persons = keyTaskService.getAllLeadPersons();
        return Result.success(persons);
    }
}