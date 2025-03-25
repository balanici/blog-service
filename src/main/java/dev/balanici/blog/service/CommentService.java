package dev.balanici.blog.service;

import dev.balanici.blog.entity.CommentEntity;
import dev.balanici.blog.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    Comment createComment(UUID postId, Comment comment);

    Comment updateComment(UUID commentId, Comment comment);

    void deleteComment(UUID commentId, UUID postId);

    // Soft delete a comment (mark as deleted without removing from database)
//    void softDeleteComment(UUID commentId);

    Comment getCommentById(UUID commentId);

    List<Comment> getCommentsByPostId(UUID postId);

    // Get paginated comments for a post
//    Page<Comment> getCommentsByPostId(UUID postId, Pageable pageable);

    // Get comments by a specific user
//    List<Comment> getCommentsByUserId(UUID userId);

    // Get comments within a date range
//    List<Comment> getCommentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    // Approve a comment (for moderation workflow)
//    Comment approveComment(UUID commentId);

    // Reject a comment
//    Comment rejectComment(UUID commentId);

    // Remove inappropriate content from a comment
//    Comment sanitizeCommentContent(UUID commentId);


    // Add a reply to a comment (nested comments)
//    Comment addReplyToComment(UUID parentCommentId, Comment replyComment);

    // Get all replies to a specific comment
//    List<Comment> getRepliesByCommentId(UUID parentCommentId);

    // Search comments by content
//    List<Comment> searchCommentsByContent(String searchTerm);

    // Filter comments by status (approved, pending, rejected)
//    List<Comment> getCommentsByStatus(CommentStatus status);

    // Get most recent comments across all posts
//    List<Comment> getRecentComments(int limit);

    long countCommentsByPostId(UUID postId);

    // Count total comments by a user
//    long countCommentsByUserId(UUID userId);

    // Get comment statistics (total, approved, rejected)
//    CommentStatistics getCommentStatistics();

    CommentEntity findCommentEntity(UUID commentId);

}
