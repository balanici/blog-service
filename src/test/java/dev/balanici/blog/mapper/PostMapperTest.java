package dev.balanici.blog.mapper;

import dev.balanici.blog.entity.CommentEntity;
import dev.balanici.blog.entity.PostEntity;
import dev.balanici.blog.model.Comment;
import dev.balanici.blog.model.Post;
import dev.balanici.blog.model.PostStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class PostMapperTest {

    @Autowired
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);


    @Test
    void toPost() {
        UUID postId = UUID.randomUUID();
        PostEntity postEntity = new PostEntity();
        postEntity.setId(postId);
        postEntity.setTitle("Test Title");
        postEntity.setContent("Test Content");
        postEntity.setPostStatus(PostStatus.PUBLISHED);
        postEntity.setCreatedAt(LocalDateTime.now());
        postEntity.setUpdatedAt(LocalDateTime.now());

        // Create and add a comment entity
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(UUID.randomUUID());
        commentEntity.setContent("Comment Content");
        commentEntity.setPostEntity(postEntity);
        // Assume that the comment's postEntity is set to the same post for mapping purposes
        postEntity.setComments(List.of(commentEntity));

        Post post = postMapper.toPost(postEntity);
        Assertions.assertThat(post).isNotNull();
        Assertions.assertThat(post.getId()).isEqualTo(postId);
        Assertions.assertThat(post.getTitle()).isEqualTo("Test Title");
        Assertions.assertThat(post.getContent()).isEqualTo("Test Content");
        Assertions.assertThat(post.getPostStatus()).isEqualTo(PostStatus.PUBLISHED);
        Assertions.assertThat(post.getComments()).hasSize(1);

        Comment comment = post.getComments().get(0);
        // In CommentMapper: mapping from commentEntity.postEntity.id to comment.postId.
        Assertions.assertThat(comment.getPostId()).isEqualTo(postId);
        Assertions.assertThat(comment.getContent()).isEqualTo("Comment Content");
    }

    @Test
    void toPostEntity() {
        UUID postId = UUID.randomUUID();
        Post post = new Post();
        post.setId(postId);
        post.setTitle("Test Title");
        post.setContent("Test Content");
        post.setPostStatus(PostStatus.PUBLISHED);

        // Create and add a comment model
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID());
        comment.setContent("Comment Content");
        comment.setPostId(postId);
        post.setComments(List.of(comment));

        PostEntity postEntity = postMapper.toPostEntity(post);
        Assertions.assertThat(postEntity).isNotNull();
        Assertions.assertThat(postEntity.getId()).isEqualTo(postId);
        Assertions.assertThat(postEntity.getTitle()).isEqualTo("Test Title");
        Assertions.assertThat(postEntity.getContent()).isEqualTo("Test Content");
        Assertions.assertThat(postEntity.getPostStatus()).isEqualTo(PostStatus.PUBLISHED);
        Assertions.assertThat(postEntity.getComments()).hasSize(1);

        CommentEntity commentEntity = postEntity.getComments().get(0);
        // Mapping in CommentMapper sets commentEntity.postEntity.id using comment.postId.
        Assertions.assertThat(commentEntity.getPostEntity()).isNotNull();
        Assertions.assertThat(commentEntity.getPostEntity().getId()).isEqualTo(postId);
        Assertions.assertThat(commentEntity.getContent()).isEqualTo("Comment Content");
    }

}
