package dev.balanici.blog.service;

import dev.balanici.blog.model.Comment;

import java.util.List;

public interface CommentService {

    // Create a new comment
    Comment createComment(Long postId, Comment comment);

    // Update an existing comment
    Comment updateComment(Long commentId, Comment comment);

    // Delete a specific comment
    void deleteComment(Long commentId);

    // Soft delete a comment (mark as deleted without removing from database)
//    void softDeleteComment(Long commentId);

    // Get a specific comment by its ID
    Comment getCommentById(Long commentId);

    // Get all comments for a specific post
    List<Comment> getCommentsByPostId(Long postId);

    // Get paginated comments for a post
//    Page<Comment> getCommentsByPostId(Long postId, Pageable pageable);

    // Get comments by a specific user
//    List<Comment> getCommentsByUserId(Long userId);

    // Get comments within a date range
//    List<Comment> getCommentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    // Approve a comment (for moderation workflow)
//    Comment approveComment(Long commentId);

    // Reject a comment
//    Comment rejectComment(Long commentId);

    // Remove inappropriate content from a comment
//    Comment sanitizeCommentContent(Long commentId);


    // Add a reply to a comment (nested comments)
//    Comment addReplyToComment(Long parentCommentId, Comment replyComment);

    // Get all replies to a specific comment
//    List<Comment> getRepliesByCommentId(Long parentCommentId);

    // Search comments by content
//    List<Comment> searchCommentsByContent(String searchTerm);

    // Filter comments by status (approved, pending, rejected)
//    List<Comment> getCommentsByStatus(CommentStatus status);

    // Get most recent comments across all posts
//    List<Comment> getRecentComments(int limit);

    // Count total comments for a post
    long countCommentsByPostId(Long postId);

    // Count total comments by a user
//    long countCommentsByUserId(Long userId);

    // Get comment statistics (total, approved, rejected)
//    CommentStatistics getCommentStatistics();


}
