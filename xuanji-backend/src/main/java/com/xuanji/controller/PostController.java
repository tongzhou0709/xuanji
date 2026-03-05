package com.xuanji.controller;

import com.xuanji.common.Result;
import com.xuanji.entity.Post;
import com.xuanji.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/list")
    public Result<List<Post>> list() {
        return Result.success(postService.list());
    }

    @PostMapping
    public Result<Void> save(@RequestBody Post post) {
        postService.savePost(post);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Post post) {
        postService.updatePost(post);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        postService.deletePost(id);
        return Result.success();
    }
}
