package com.xuanji.controller;

import com.xuanji.common.Result;
import com.xuanji.entity.Org;
import com.xuanji.service.OrgService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/org")
@RequiredArgsConstructor
public class OrgController {

    private final OrgService orgService;

    @GetMapping("/list")
    public Result<List<Org>> list() {
        return Result.success(orgService.list());
    }

    @PostMapping
    public Result<Void> save(@RequestBody Org org) {
        orgService.saveOrg(org);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Org org) {
        orgService.updateOrg(org);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        orgService.deleteOrg(id);
        return Result.success();
    }
}
