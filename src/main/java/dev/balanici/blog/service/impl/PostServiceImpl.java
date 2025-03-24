package dev.balanici.blog.service.impl;

import dev.balanici.blog.model.Post;
import dev.balanici.blog.repository.PostRepository;
import dev.balanici.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return List.of();
    }

    @Override
    public Post createPost(Post post) {
        return null;
    }

    @Override
    public Post getPostById(UUID id) {
        return null;
    }

    @Override
    public Post updatePost(UUID id, Post post) {
        return null;
    }

    @Override
    public void deletePost(UUID id) {

    }
}
