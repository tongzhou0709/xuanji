package com.xuanji.service;

import com.xuanji.entity.Post;
import java.util.List;

public interface PostService {
    List<Post> list();
    void savePost(Post post);
    void updatePost(Post post);
    void deletePost(Long id);
}
