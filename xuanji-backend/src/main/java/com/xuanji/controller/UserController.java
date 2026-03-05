package com.xuanji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuanji.common.Result;
import com.xuanji.dto.UserImportDTO;
import com.xuanji.entity.User;
import com.xuanji.entity.Role;
import com.xuanji.service.UserService;
import com.xuanji.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/page")
    public Result<Page<User>> page(@RequestParam(defaultValue = "1") Integer current,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   User user) {
        Page<User> page = new Page<>(current, size);
        return Result.success(userService.page(page, user));
    }

    @PostMapping
    public Result<Void> save(@RequestBody User user) {
        userService.saveUser(user);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @PostMapping("/password")
    public Result<Void> updatePassword(@RequestParam Long userId,
                                       @RequestParam String oldPassword,
                                       @RequestParam String newPassword) {
        userService.updatePassword(userId, oldPassword, newPassword);
        return Result.success();
    }

    @GetMapping("/{id}/roles")
    public Result<List<Role>> getUserRoles(@PathVariable Long id) {
        return Result.success(roleService.getRolesByUserId(id));
    }

    @PostMapping("/{id}/roles")
    public Result<Void> assignUserRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        roleService.assignUserRoles(id, roleIds);
        return Result.success();
    }

    @PostMapping("/import")
    public Result<String> importUsers(@RequestParam("file") MultipartFile file) {
        int count = userService.importUsers(file);
        return Result.success("成功导入 " + count + " 条数据");
    }
}
