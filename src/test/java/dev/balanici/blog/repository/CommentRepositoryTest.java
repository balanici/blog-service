package dev.balanici.blog.repository;

import dev.balanici.blog.entity.CommentEntity;
import dev.balanici.blog.entity.PostEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static dev.balanici.blog.model.PostStatus.DRAFT;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EntityScan("dev.balanici.blog.entity")
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    void testFindAllByPostEntityId() {
        // Create and persist a PostEntity
        PostEntity post = new PostEntity();
        post.setTitle("Test Title");
        post.setContent("Test Content");
        post.setPostStatus(DRAFT);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        // Persist the post so that it is managed and gets an id
        post = entityManager.persistAndFlush(post);

        // Create and persist a CommentEntity associated with the post
        CommentEntity comment = new CommentEntity();
        comment.setContent("Test Comment");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setPostEntity(post);
        entityManager.persistAndFlush(comment);

        // Retrieve comments using repository method
        List<CommentEntity> comments = commentRepository.findAllByPostEntityId(post.getId());
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getContent()).isEqualTo("Test Comment");
    }

    @Test
    void testDeleteById() {
        // Create and persist a PostEntity
        PostEntity post = new PostEntity();
        post.setContent("Test Content");
        post.setTitle("Test Title");
        post.setContent("Test Content");
        post.setPostStatus(DRAFT);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post = entityManager.persistAndFlush(post);

        // Create and persist a CommentEntity
        CommentEntity comment = new CommentEntity();
        comment.setContent("Delete me");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setPostEntity(post);
        comment = entityManager.persistAndFlush(comment);

        // Ensure the comment exists
        Optional<CommentEntity> found = commentRepository.findById(comment.getId());
        assertThat(found).isPresent();

        // Delete the comment and verify deletion
        commentRepository.deleteById(comment.getId());
        entityManager.flush();

        Optional<CommentEntity> deleted = commentRepository.findById(comment.getId());
        assertThat(deleted).isNotPresent();
    }

}
