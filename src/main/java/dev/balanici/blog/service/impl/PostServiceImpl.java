package dev.balanici.blog.service.impl;

import dev.balanici.blog.entity.PostEntity;
import dev.balanici.blog.mapper.PostMapper;
import dev.balanici.blog.model.Post;
import dev.balanici.blog.repository.PostRepository;
import dev.balanici.blog.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static dev.balanici.blog.model.PostStatus.DRAFT;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAll().stream()
                .map(postMapper::toPost)
                .toList();
    }

    @Override
    @Transactional
    public Post createPost(Post post) {
        PostEntity postEntity = postMapper.toPostEntity(post);
        postEntity.setPostStatus(
                post.getPostStatus() == null ? DRAFT : postEntity.getPostStatus()
        );
        postEntity.setCreatedAt(LocalDateTime.now());
        postEntity.setUpdatedAt(LocalDateTime.now());
        PostEntity saved = postRepository.save(postEntity);
        return postMapper.toPost(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPostById(UUID id) {
        return postMapper.toPost(findPostEntityById(id));
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, Post post) {
        PostEntity postEntity = findPostEntityById(id);
        postEntity.setTitle(post.getTitle());
        postEntity.setContent(post.getContent());
        postEntity.setPostStatus(ofNullable(post.getPostStatus()).orElse(DRAFT));
        postEntity.setUpdatedAt(LocalDateTime.now());
        return postMapper.toPost(postRepository.save(postEntity));
    }

    @Override
    @Transactional
    public void deletePost(UUID id) {
        postRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PostEntity findPostEntityById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + id + " not found"));
    }

}
