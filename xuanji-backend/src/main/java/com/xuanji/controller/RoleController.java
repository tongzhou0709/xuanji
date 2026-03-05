package com.xuanji.controller;

import com.xuanji.common.Result;
import com.xuanji.entity.Menu;
import com.xuanji.entity.Role;
import com.xuanji.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/list")
    public Result<List<Role>> list() {
        return Result.success(roleService.list());
    }

    @PostMapping
    public Result<Void> save(@RequestBody Role role) {
        roleService.saveRole(role);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Role role) {
        roleService.updateRole(role);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success();
    }

    @GetMapping("/{id}/menus")
    public Result<List<Menu>> getMenus(@PathVariable Long id) {
        return Result.success(roleService.getMenusByRoleId(id));
    }

    @PostMapping("/{id}/menus")
    public Result<Void> assignMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) {
        roleService.assignMenus(id, menuIds);
        return Result.success();
    }
}
