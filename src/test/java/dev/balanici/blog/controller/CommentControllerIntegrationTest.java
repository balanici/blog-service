package dev.balanici.blog.controller;

import dev.balanici.blog.model.Comment;
import dev.balanici.blog.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
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
class CommentControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.8-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    private Post createTestPost() {
        Post post = new Post();
        post.setTitle("Integration Test Post");
        post.setContent("Test Content");
        ResponseEntity<Post> response = restTemplate.postForEntity("/api/posts", post, Post.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        return response.getBody();
    }

    @BeforeEach
    void setup() {
        // Ensure we have a fresh Post for every test since comments require an associated postId.
        assertNotNull(createTestPost());
    }

    @Test
    void testCreateAndRetrieveComment() {
        Post post = createTestPost();

        // Create a new Comment
        Comment newComment = new Comment();
        newComment.setContent("Test Comment Content");
        ResponseEntity<Comment> createResponse = restTemplate.postForEntity(
                "/api/posts/" + post.getId() + "/comments", newComment, Comment.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Comment createdComment = createResponse.getBody();
        assertNotNull(createdComment);
        UUID commentId = createdComment.getId();

        // Retrieve the created Comment by id
        ResponseEntity<Comment> getResponse = restTemplate.getForEntity(
                "/api/posts/" + post.getId() + "/comments/" + commentId, Comment.class);
        assertTrue(getResponse.getStatusCode().is2xxSuccessful());
        Comment retrievedComment = getResponse.getBody();
        assertNotNull(retrievedComment);
        assertEquals("Test Comment Content", retrievedComment.getContent());

        // Retrieve comments by postId
        ResponseEntity<Comment[]> listResponse = restTemplate.getForEntity(
                "/api/posts/" + post.getId() + "/comments", Comment[].class);
        assertTrue(listResponse.getStatusCode().is2xxSuccessful());
        Comment[] comments = listResponse.getBody();
        assertNotNull(comments);
        assertTrue(Arrays.stream(comments).anyMatch(c -> c.getId().equals(commentId)));
    }

    @Test
    void testUpdateComment() {
        Post post = createTestPost();

        // Create a Comment
        Comment newComment = new Comment();
        newComment.setContent("Original Comment");
        ResponseEntity<Comment> createResponse = restTemplate.postForEntity(
                "/api/posts/" + post.getId() + "/comments", newComment, Comment.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Comment createdComment = createResponse.getBody();
        assertNotNull(createdComment);
        UUID commentId = createdComment.getId();

        // Update the Comment
        createdComment.setContent("Updated Comment Content");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Comment> updateEntity = new HttpEntity<>(createdComment, headers);
        ResponseEntity<Comment> updateResponse = restTemplate.exchange(
                "/api/posts/" + post.getId() + "/comments/" + commentId,
                HttpMethod.PUT, updateEntity, Comment.class);
        assertTrue(updateResponse.getStatusCode().is2xxSuccessful());
        Comment updatedComment = updateResponse.getBody();
        assertNotNull(updatedComment);
        assertEquals("Updated Comment Content", updatedComment.getContent());
    }

    @Test
    void testDeleteComment() {
        Post post = createTestPost();

        // Create a Comment
        Comment newComment = new Comment();
        newComment.setContent("Comment to be deleted");
        ResponseEntity<Comment> createResponse = restTemplate.postForEntity(
                "/api/posts/" + post.getId() + "/comments", newComment, Comment.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Comment createdComment = createResponse.getBody();
        assertNotNull(createdComment);
        UUID commentId = createdComment.getId();

        // Delete the Comment
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/posts/" + post.getId() + "/comments/" + commentId,
                HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Verify deletion by attempting to retrieve the comment
        ResponseEntity<Comment> getResponse = restTemplate.getForEntity(
                "/api/posts/" + post.getId() + "/comments/" + commentId, Comment.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testCountComments() {
        Post post = createTestPost();

        // Create two Comments
        Comment comment1 = new Comment();
        comment1.setContent("First Comment");
        ResponseEntity<Comment> response1 = restTemplate.postForEntity(
                "/api/posts/" + post.getId() + "/comments", comment1, Comment.class);
        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        Comment comment2 = new Comment();
        comment2.setContent("Second Comment");
        ResponseEntity<Comment> response2 = restTemplate.postForEntity(
                "/api/posts/" + post.getId() + "/comments", comment2, Comment.class);
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());

        // Get comment count
        ResponseEntity<Long> countResponse = restTemplate.getForEntity(
                "/api/posts/" + post.getId() + "/comments/count", Long.class);
        assertTrue(countResponse.getStatusCode().is2xxSuccessful());
        Long count = countResponse.getBody();
        assertNotNull(count);
        // At least 2 comments should have been created
        assertTrue(count >= 2);
    }
}
