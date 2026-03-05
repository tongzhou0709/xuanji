package com.xuanji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuanji.entity.Post;
import com.xuanji.mapper.PostMapper;
import com.xuanji.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    @Override
    public List<Post> list() {
        return postMapper.selectList(new LambdaQueryWrapper<Post>().orderByAsc(Post::getPostSort));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePost(Post post) {
        postMapper.insert(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePost(Post post) {
        postMapper.updateById(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long id) {
        postMapper.deleteById(id);
    }
}
