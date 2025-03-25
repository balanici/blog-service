package dev.balanici.blog.controller;

import dev.balanici.blog.model.Comment;
import dev.balanici.blog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    // GET: Retrieve all comments for a specific post
    @GetMapping
    public ResponseEntity<List<Comment>> getCommentsByPostId(
            @PathVariable UUID postId) {
        // Implementation
    }


    @PostMapping
    public ResponseEntity<Comment> createComment(
            @PathVariable UUID postId,
            @RequestBody Comment comment) {
        // Implementation
    }

    // GET: Retrieve a specific comment by its ID
    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getCommentById(
            @PathVariable UUID postId,
            @PathVariable UUID commentId) {
        // Implementation
    }


    // GET: Count total comments for a specific post
    @GetMapping("/count")
    public ResponseEntity<Long> countCommentsByPostId(
            @PathVariable UUID postId) {
        // Implementation
    }

    // PUT: Update an existing comment
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable UUID postId,
            @PathVariable UUID commentId,
            @RequestBody Comment comment) {
        // Implementation
    }

    // DELETE: Delete a specific comment
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable UUID postId,
            @PathVariable UUID commentId) {
        // Implementation
    }

}
