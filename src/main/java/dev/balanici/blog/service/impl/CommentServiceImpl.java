package dev.balanici.blog.service.impl;

import dev.balanici.blog.model.Comment;
import dev.balanici.blog.repository.CommentRepository;
import dev.balanici.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment createComment(Long postId, Comment comment) {
        return null;
    }

    @Override
    public Comment updateComment(Long commentId, Comment comment) {
        return null;
    }

    @Override
    public void deleteComment(Long commentId) {

    }

    @Override
    public Comment getCommentById(Long commentId) {
        return null;
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return List.of();
    }

    @Override
    public long countCommentsByPostId(Long postId) {
        return 0;
    }
}
