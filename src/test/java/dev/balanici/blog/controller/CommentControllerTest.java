package dev.balanici.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.balanici.blog.model.Comment;
import dev.balanici.blog.service.CommentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetCommentsByPostId() throws Exception {
        UUID postId = UUID.randomUUID();
        Comment sampleComment = new Comment();
        sampleComment.setId(UUID.randomUUID());
        sampleComment.setContent("Test comment");

        Mockito.when(commentService.getCommentsByPostId(eq(postId)))
                .thenReturn(Collections.singletonList(sampleComment));

        mockMvc.perform(get("/api/posts/{postId}/comments", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(sampleComment.getId().toString()))
                .andExpect(jsonPath("$[0].content").value("Test comment"));
    }

    @Test
    void testCreateComment() throws Exception {
        UUID postId = UUID.randomUUID();
        Comment requestComment = new Comment();
        requestComment.setContent("New Comment");

        Comment savedComment = new Comment();
        savedComment.setId(UUID.randomUUID());
        savedComment.setContent("New Comment");

        Mockito.when(commentService.createComment(eq(postId), any(Comment.class)))
                .thenReturn(savedComment);

        mockMvc.perform(post("/api/posts/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestComment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedComment.getId().toString()))
                .andExpect(jsonPath("$.content").value("New Comment"));
    }

    @Test
    void testGetCommentById() throws Exception {
        UUID postId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();
        Comment sampleComment = new Comment();
        sampleComment.setId(commentId);
        sampleComment.setContent("Sample Comment");

        Mockito.when(commentService.getCommentById(eq(commentId)))
                .thenReturn(sampleComment);

        mockMvc.perform(get("/api/posts/{postId}/comments/{commentId}", postId, commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId.toString()))
                .andExpect(jsonPath("$.content").value("Sample Comment"));
    }

    @Test
    void testUpdateComment() throws Exception {
        UUID postId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();
        Comment requestComment = new Comment();
        requestComment.setContent("Updated Comment");

        Comment updatedComment = new Comment();
        updatedComment.setId(commentId);
        updatedComment.setContent("Updated Comment");

        Mockito.when(commentService.updateComment(eq(commentId), any(Comment.class)))
                .thenReturn(updatedComment);

        mockMvc.perform(put("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId.toString()))
                .andExpect(jsonPath("$.content").value("Updated Comment"));
    }

    @Test
    void testDeleteComment() throws Exception {
        UUID postId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();

        Mockito.doNothing().when(commentService).deleteComment(eq(commentId), eq(postId));

        mockMvc.perform(delete("/api/posts/{postId}/comments/{commentId}", postId, commentId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCountCommentsByPostId() throws Exception {
        UUID postId = UUID.randomUUID();
        Long count = 5L;

        Mockito.when(commentService.countCommentsByPostId(eq(postId)))
                .thenReturn(count);

        mockMvc.perform(get("/api/posts/{postId}/comments/count", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(count));
    }
}
