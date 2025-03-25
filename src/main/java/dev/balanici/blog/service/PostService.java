package dev.balanici.blog.service;

import dev.balanici.blog.entity.PostEntity;
import dev.balanici.blog.model.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<Post> getAllPosts();

    Post createPost(Post post);

    Post getPostById(UUID id);

    Post updatePost(UUID id, Post post);

    void deletePost(UUID id);

    PostEntity findPostEntityById(UUID id);
}
