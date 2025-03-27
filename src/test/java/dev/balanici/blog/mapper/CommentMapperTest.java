package dev.balanici.blog.mapper;

import dev.balanici.blog.entity.CommentEntity;
import dev.balanici.blog.entity.PostEntity;
import dev.balanici.blog.model.Comment;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentMapperTest {

    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @Test
    void toComment() {
        // Prepare a CommentEntity with a nested PostEntity
        UUID postId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();

        PostEntity postEntity = new PostEntity();
        postEntity.setId(postId);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(commentId);
        commentEntity.setContent("Test Comment");
        commentEntity.setPostEntity(postEntity);

        // Execute mapping
        Comment comment = commentMapper.toComment(commentEntity);

        // Validate results
        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isEqualTo(commentId);
        assertThat(comment.getContent()).isEqualTo("Test Comment");
        // Verify that the mapped Comment's postId equals the PostEntity's id
        assertThat(comment.getPostId()).isEqualTo(postId);
    }

    @Test
    void toCommentEntity() {
        // Prepare a Comment with a postId value
        UUID postId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setContent("Test Comment");
        comment.setPostId(postId);

        // Execute mapping
        CommentEntity commentEntity = commentMapper.toCommentEntity(comment);

        // Validate results
        assertThat(commentEntity).isNotNull();
        assertThat(commentEntity.getId()).isEqualTo(commentId);
        assertThat(commentEntity.getContent()).isEqualTo("Test Comment");
        // Verify that the mapped CommentEntity has a non-null PostEntity with the same id as comment.postId
        assertThat(commentEntity.getPostEntity()).isNotNull();
        assertThat(commentEntity.getPostEntity().getId()).isEqualTo(postId);
    }
}
