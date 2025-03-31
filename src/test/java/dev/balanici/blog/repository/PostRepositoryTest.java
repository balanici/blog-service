package dev.balanici.blog.repository;

import dev.balanici.blog.entity.PostEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static dev.balanici.blog.model.PostStatus.DRAFT;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EntityScan("dev.balanici.blog.entity")
@ExtendWith(SpringExtension.class)
@Testcontainers
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.8-alpine")
            .withDatabaseName("testdb").withUsername("testuser").withPassword("testpassword");

    @DynamicPropertySource
    static void overrideProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void testSaveAndFindById() {
        PostEntity post = new PostEntity();
        post.setTitle("Test Title");
        post.setContent("Test Content");
        post.setPostStatus(DRAFT);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post = postRepository.save(post);

        Optional<PostEntity> found = postRepository.findById(post.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getContent()).isEqualTo("Test Content");
    }

    @Test
    void testUpdateContent() {
        PostEntity post = new PostEntity();
        post.setTitle("Test Title");
        post.setContent("Old Content");
        post.setPostStatus(DRAFT);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post = postRepository.save(post);

        post.setContent("Updated Content");
        post = postRepository.save(post);

        Optional<PostEntity> found = postRepository.findById(post.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getContent()).isEqualTo("Updated Content");
    }

    @Test
    void testDeleteById() {
        PostEntity post = new PostEntity();
        post.setTitle("Test Title");
        post.setContent("Content to be deleted");
        post.setPostStatus(DRAFT);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post = postRepository.save(post);

        postRepository.deleteById(post.getId());
        Optional<PostEntity> deleted = postRepository.findById(post.getId());
        assertThat(deleted).isNotPresent();
    }
}
