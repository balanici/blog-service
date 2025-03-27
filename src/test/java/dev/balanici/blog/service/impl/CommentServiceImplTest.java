package dev.balanici.blog.service.impl;

import dev.balanici.blog.entity.CommentEntity;
import dev.balanici.blog.entity.PostEntity;
import dev.balanici.blog.mapper.CommentMapper;
import dev.balanici.blog.model.Comment;
import dev.balanici.blog.repository.CommentRepository;
import dev.balanici.blog.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private PostService postService;

    @InjectMocks
    private CommentServiceImpl commentService;

    private UUID commentId;
    private UUID postId;
    private Comment comment;
    private CommentEntity commentEntity;
    private PostEntity postEntity;

    @BeforeEach
    void setUp() {
        commentId = UUID.randomUUID();
        postId = UUID.randomUUID();

        comment = new Comment();
        comment.setId(commentId);
        comment.setContent("Test Content");

        commentEntity = new CommentEntity();
        commentEntity.setId(commentId);
        commentEntity.setContent("Test Content");

        postEntity = new PostEntity();
        postEntity.setId(postId);
    }

    @Test
    void createComment_shouldSaveAndReturnComment() {
        // Arrange
        when(postService.findPostEntityById(postId)).thenReturn(postEntity);
        when(commentMapper.toCommentEntity(comment)).thenReturn(commentEntity);
        when(commentRepository.save(any(CommentEntity.class))).thenAnswer(invocation -> {
            CommentEntity saved = invocation.getArgument(0);
            saved.setId(commentId);
            return saved;
        });
        when(commentMapper.toComment(any(CommentEntity.class))).thenReturn(comment);

        // Act
        Comment result = commentService.createComment(postId, comment);

        // Assert
        ArgumentCaptor<CommentEntity> captor = ArgumentCaptor.forClass(CommentEntity.class);
        verify(commentRepository).save(captor.capture());
        CommentEntity captured = captor.getValue();
        assertThat(captured.getPostEntity()).isEqualTo(postEntity);
        assertThat(captured.getCreatedAt()).isNotNull();
        assertThat(captured.getUpdatedAt()).isNotNull();
        assertThat(result).isEqualTo(comment);
    }

    @Test
    void getCommentsByPostId_shouldReturnMappedComments() {
        // Arrange
        List<CommentEntity> commentEntities = List.of(commentEntity);
        when(commentRepository.findAllByPostEntityId(postId)).thenReturn(commentEntities);
        when(commentMapper.toComment(commentEntity)).thenReturn(comment);

        // Act
        List<Comment> result = commentService.getCommentsByPostId(postId);

        // Assert
        assertThat(result).hasSize(1).containsExactly(comment);
    }

    @Test
    void updateComment_shouldUpdateAndReturnComment() {
        // Arrange
        Comment updatedComment = new Comment();
        updatedComment.setContent("Updated Content");

        CommentEntity existingEntity = new CommentEntity();
        existingEntity.setId(commentId);
        existingEntity.setContent("Old Content");
        existingEntity.setCreatedAt(LocalDateTime.now().minusDays(1));
        existingEntity.setUpdatedAt(LocalDateTime.now().minusDays(1));

        CommentEntity updatedEntity = new CommentEntity();
        updatedEntity.setId(commentId);
        updatedEntity.setContent("Updated Content");
        updatedEntity.setCreatedAt(existingEntity.getCreatedAt());
        updatedEntity.setUpdatedAt(LocalDateTime.now());

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingEntity));
        when(commentRepository.save(existingEntity)).thenReturn(updatedEntity);
        when(commentMapper.toComment(updatedEntity)).thenReturn(updatedComment);

        // Act
        Comment result = commentService.updateComment(commentId, updatedComment);

        // Assert
        assertThat(result.getContent()).isEqualTo("Updated Content");
        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(existingEntity);
    }

    @Test
    void deleteComment_shouldCallCommentRepositoryDeleteById() {
        // Act
        commentService.deleteComment(commentId, postId);

        // Assert
        verify(commentRepository).deleteById(commentId);
    }

    @Test
    void getCommentById_shouldReturnMappedComment() {
        // Arrange
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        when(commentMapper.toComment(commentEntity)).thenReturn(comment);

        // Act
        Comment result = commentService.getCommentById(commentId);

        // Assert
        assertThat(result).isEqualTo(comment);
    }

    @Test
    void countCommentsByPostId_shouldReturnCorrectCount() {
        // Arrange
        PostEntity postWithComments = new PostEntity();
        postWithComments.setId(postId);
        postWithComments.setComments(List.of(commentEntity, new CommentEntity()));
        when(postService.findPostEntityById(postId)).thenReturn(postWithComments);

        // Act
        long count = commentService.countCommentsByPostId(postId);

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    void findCommentEntity_shouldReturnEntity_whenFound() {
        // Arrange
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));

        // Act
        CommentEntity result = commentService.findCommentEntity(commentId);

        // Assert
        assertThat(result).isEqualTo(commentEntity);
    }

    @Test
    void findCommentEntity_shouldThrowException_whenNotFound() {
        // Arrange
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> commentService.findCommentEntity(commentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Comment with id " + commentId + " not found");
    }

}
