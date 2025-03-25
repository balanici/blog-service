package dev.balanici.blog.service.impl;

import dev.balanici.blog.entity.CommentEntity;
import dev.balanici.blog.entity.PostEntity;
import dev.balanici.blog.mapper.CommentMapper;
import dev.balanici.blog.model.Comment;
import dev.balanici.blog.repository.CommentRepository;
import dev.balanici.blog.service.CommentService;
import dev.balanici.blog.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostService postService;

    @Override
    public Comment createComment(UUID postId, Comment comment) {
        PostEntity postEntity = postService.findPostEntityById(postId);
        CommentEntity commentEntity = commentMapper.toCommentEntity(comment);
        commentEntity.setPostEntity(postEntity);
        commentEntity.setCreatedAt(LocalDateTime.now());
        commentEntity.setUpdatedAt(LocalDateTime.now());

        return commentMapper.toComment(commentRepository.save(commentEntity));
    }

    @Override
    public List<Comment> getCommentsByPostId(UUID postId) {
        return commentRepository.findAllByPostEntityId(postId)
                .stream().map(commentMapper::toComment)
                .toList();
    }

    @Override
    public Comment updateComment(UUID commentId, Comment comment) {
        CommentEntity commentEntity = findCommentEntity(commentId);
        commentEntity.setContent(comment.getContent());
        commentEntity.setUpdatedAt(LocalDateTime.now());

        return commentMapper.toComment(commentRepository.save(commentEntity));
    }


    @Override
    public void deleteComment(UUID commentId, UUID postId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment getCommentById(UUID commentId) {
        return commentMapper.toComment(findCommentEntity(commentId));
    }

    @Override
    public long countCommentsByPostId(UUID postId) {
        return postService.findPostEntityById(postId).getComments().size();
    }


    @Override
    public CommentEntity findCommentEntity(UUID commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("Comment with id " + commentId + " not found")
        );
    }
}
