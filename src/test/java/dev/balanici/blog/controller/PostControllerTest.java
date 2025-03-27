package dev.balanici.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.balanici.blog.model.Post;
import dev.balanici.blog.service.PostService;
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

@WebMvcTest(PostController.class)
class PostControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllPosts() throws Exception {
        Post samplePost = new Post();
        samplePost.setId(UUID.randomUUID());
        samplePost.setContent("Test Content");

        Mockito.when(postService.getAllPosts()).thenReturn(Collections.singletonList(samplePost));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(samplePost.getId().toString()))
                .andExpect(jsonPath("$[0].content").value("Test Content"));
    }

    @Test
    void testCreatePost() throws Exception {
        Post requestPost = new Post();
        requestPost.setContent("New Post Content");

        Post savedPost = new Post();
        savedPost.setId(UUID.randomUUID());
        savedPost.setContent("New Post Content");

        Mockito.when(postService.createPost(any(Post.class))).thenReturn(savedPost);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestPost)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedPost.getId().toString()))
                .andExpect(jsonPath("$.content").value("New Post Content"));
    }

    @Test
    void testGetPostById() throws Exception {
        UUID postId = UUID.randomUUID();
        Post samplePost = new Post();
        samplePost.setId(postId);
        samplePost.setContent("Sample Content");

        Mockito.when(postService.getPostById(eq(postId))).thenReturn(samplePost);

        mockMvc.perform(get("/api/posts/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId.toString()))
                .andExpect(jsonPath("$.content").value("Sample Content"));
    }

    @Test
    void testUpdatePost() throws Exception {
        UUID postId = UUID.randomUUID();
        Post requestPost = new Post();
        requestPost.setContent("Updated Content");

        Post updatedPost = new Post();
        updatedPost.setId(postId);
        updatedPost.setContent("Updated Content");

        Mockito.when(postService.updatePost(eq(postId), any(Post.class))).thenReturn(updatedPost);

        mockMvc.perform(put("/api/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestPost)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId.toString()))
                .andExpect(jsonPath("$.content").value("Updated Content"));
    }

    @Test
    void testDeletePost() throws Exception {
        UUID postId = UUID.randomUUID();

        Mockito.doNothing().when(postService).deletePost(postId);

        mockMvc.perform(delete("/api/posts/{id}", postId))
                .andExpect(status().isNoContent());
    }

}
