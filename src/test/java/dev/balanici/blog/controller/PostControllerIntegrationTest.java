package dev.balanici.blog.controller;

import dev.balanici.blog.model.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class PostControllerIntegrationTest {
    // This class is used for integration testing of the PostController
    // It will use Testcontainers to spin up a real database and test the controller against it
    // You can add your integration tests here

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.8-alpine")
            .withDatabaseName("testdb").withUsername("testuser").withPassword("testpassword");

    @DynamicPropertySource
    static void overrideProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    // Example test method
    @Test
    void contextLoads() {
        // This test will simply check if the application context loads successfully
        assertTrue(true);
    }

    @Test
    void testCreateAndRetrievePost() {
        // Create a Post
        Post newPost = new Post();
        newPost.setTitle("Integration Test Post");
        newPost.setContent("Integration Test Content");

        ResponseEntity<Post> postResponse = restTemplate.postForEntity("/api/posts", newPost, Post.class);
        assertTrue(postResponse.getStatusCode().is2xxSuccessful());
        Post createdPost = postResponse.getBody();
        assertNotNull(createdPost);
        assertNotNull(createdPost.getId());
        assertEquals("Integration Test Content", createdPost.getContent());

        // Retrieve the created Post
        ResponseEntity<Post> getResponse = restTemplate.getForEntity("/api/posts/" + createdPost.getId(), Post.class);
        assertTrue(getResponse.getStatusCode().is2xxSuccessful());
        Post retrievedPost = getResponse.getBody();
        assertNotNull(retrievedPost);
        assertEquals(createdPost.getId(), retrievedPost.getId());
        assertEquals("Integration Test Content", retrievedPost.getContent());
    }

    @Test
    void testUpdatePost() {
        // Create a Post
        Post newPost = new Post();
        newPost.setTitle("Integration Test Post");
        newPost.setContent("Original Content");
        ResponseEntity<Post> postResponse = restTemplate.postForEntity("/api/posts", newPost, Post.class);
        Post createdPost = postResponse.getBody();
        assertNotNull(createdPost);
        UUID postId = createdPost.getId();

        // Update the Post
        createdPost.setContent("Updated Content");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Post> httpEntity = new HttpEntity<>(createdPost, headers);
        ResponseEntity<Post> updateResponse = restTemplate.exchange("/api/posts/" + postId, HttpMethod.PUT, httpEntity, Post.class);
        assertTrue(updateResponse.getStatusCode().is2xxSuccessful());
        Post updatedPost = updateResponse.getBody();
        assertNotNull(updatedPost);
        assertEquals("Updated Content", updatedPost.getContent());
    }

    @Test
    void testDeletePost() {
        // Create a Post
        Post newPost = new Post();
        newPost.setTitle("Integration Test Post");
        newPost.setContent("Content to be deleted");
        ResponseEntity<Post> postResponse = restTemplate.postForEntity("/api/posts", newPost, Post.class);
        Post createdPost = postResponse.getBody();
        assertNotNull(createdPost);
        UUID postId = createdPost.getId();

        // Delete the Post
        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/api/posts/" + postId, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Verify deletion by trying to retrieve the deleted Post
        ResponseEntity<Post> getResponse = restTemplate.getForEntity("/api/posts/" + postId, Post.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testGetAllPosts() {
        // Create two Posts
        Post post1 = new Post();
        post1.setTitle("Integration Test Post1");
        post1.setContent("Post One Content");
        ResponseEntity<Post> response1 = restTemplate.postForEntity("/api/posts", post1, Post.class);
        assertTrue(response1.getStatusCode().is2xxSuccessful());

        Post post2 = new Post();
        post2.setTitle("Integration Test Post2");
        post2.setContent("Post Two Content");
        ResponseEntity<Post> response2 = restTemplate.postForEntity("/api/posts", post2, Post.class);
        assertTrue(response2.getStatusCode().is2xxSuccessful());

        // Retrieve all Posts
        ResponseEntity<Post[]> getResponse = restTemplate.getForEntity("/api/posts", Post[].class);
        assertTrue(getResponse.getStatusCode().is2xxSuccessful());
        Post[] posts = getResponse.getBody();
        assertNotNull(posts);
        // At least two posts should be available
        assertTrue(Arrays.stream(posts).anyMatch(post -> "Post One Content".equals(post.getContent())));
        assertTrue(Arrays.stream(posts).anyMatch(post -> "Post Two Content".equals(post.getContent())));
    }
}
