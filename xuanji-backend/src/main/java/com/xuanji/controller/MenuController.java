package com.xuanji.controller;

import com.xuanji.common.Result;
import com.xuanji.entity.Menu;
import com.xuanji.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/list")
    public Result<List<Menu>> list() {
        return Result.success(menuService.list());
    }

    @PostMapping
    public Result<Void> save(@RequestBody Menu menu) {
        menuService.saveMenu(menu);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Menu menu) {
        menuService.updateMenu(menu);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.success();
    }
}
