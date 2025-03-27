package dev.balanici.blog.service.impl;

import dev.balanici.blog.entity.PostEntity;
import dev.balanici.blog.mapper.PostMapper;
import dev.balanici.blog.model.Post;
import dev.balanici.blog.model.PostStatus;
import dev.balanici.blog.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
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

import static dev.balanici.blog.model.PostStatus.DRAFT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostServiceImpl postService;


    private UUID id;
    private PostEntity postEntity;
    private Post post;


    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        postEntity = new PostEntity();
        postEntity.setId(id);
        postEntity.setTitle("Test Title");
        postEntity.setContent("Test Content");
        postEntity.setPostStatus(DRAFT);
        postEntity.setCreatedAt(LocalDateTime.now());
        postEntity.setUpdatedAt(LocalDateTime.now());

        post = new Post();
        post.setId(id);
        post.setTitle("Test Title");
        post.setContent("Test Content");
        post.setPostStatus(null); // for createPost default
    }

    @AfterEach
    void tearDown() {
    }

    /********************************/
    @Test
    void getAllPosts_shouldReturnMappedPosts() {
        // Arrange
        when(postRepository.findAll()).thenReturn(List.of(postEntity));
        when(postMapper.toPost(postEntity)).thenReturn(post);

        // Act
        List<Post> result = postService.getAllPosts();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(post);
        verify(postRepository, times(1)).findAll();
    }

    @Test
    void createPost_shouldDefaultToDraft_whenPostStatusIsNull() {
        // Arrange
        when(postMapper.toPostEntity(post)).thenReturn(postEntity);
        when(postRepository.save(any(PostEntity.class))).thenAnswer(invocation -> {
            PostEntity saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });
        when(postMapper.toPost(any(PostEntity.class))).thenReturn(post);

        // Act
        Post result = postService.createPost(post);

        // Assert
        ArgumentCaptor<PostEntity> captor = ArgumentCaptor.forClass(PostEntity.class);
        verify(postRepository).save(captor.capture());
        PostEntity captured = captor.getValue();
        // post status should be defaulted to DRAFT when null in post entity
        assertThat(captured.getPostStatus()).isEqualTo(DRAFT);
        assertThat(result).isEqualTo(post);
    }

    @Test
    void getPostById_shouldReturnMappedPost_whenFound() {
        // Arrange
        when(postRepository.findById(id)).thenReturn(Optional.of(postEntity));
        when(postMapper.toPost(postEntity)).thenReturn(post);

        // Act
        Post result = postService.getPostById(id);

        // Assert
        assertThat(result).isEqualTo(post);
        verify(postRepository, times(1)).findById(id);
    }

    @Test
    void getPostById_shouldThrowException_whenNotFound() {
        // Arrange
        when(postRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class, () -> postService.getPostById(id));
        verify(postRepository, times(1)).findById(id);
    }

    @Test
    void updatePost_shouldUpdateFieldsAndReturnMappedPost() {
        // Arrange
        Post updatedPost = new Post();
        updatedPost.setTitle("Updated Title");
        updatedPost.setContent("Updated Content");
        updatedPost.setPostStatus(PostStatus.PUBLISHED);

        PostEntity existingEntity = new PostEntity();
        existingEntity.setId(id);
        existingEntity.setTitle("Old Title");
        existingEntity.setContent("Old Content");
        existingEntity.setPostStatus(DRAFT);
        existingEntity.setCreatedAt(LocalDateTime.now().minusDays(1));
        existingEntity.setUpdatedAt(LocalDateTime.now().minusDays(1));

        PostEntity savedEntity = new PostEntity();
        savedEntity.setId(id);
        savedEntity.setTitle("Updated Title");
        savedEntity.setContent("Updated Content");
        savedEntity.setPostStatus(PostStatus.PUBLISHED);
        savedEntity.setCreatedAt(existingEntity.getCreatedAt());
        savedEntity.setUpdatedAt(LocalDateTime.now());

        when(postRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(postRepository.save(any(PostEntity.class))).thenReturn(savedEntity);
        when(postMapper.toPost(savedEntity)).thenReturn(updatedPost);

        // Act
        Post result = postService.updatePost(id, updatedPost);

        // Assert
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getContent()).isEqualTo("Updated Content");
        assertThat(result.getPostStatus()).isEqualTo(PostStatus.PUBLISHED);
        verify(postRepository, times(1)).findById(id);
        verify(postRepository, times(1)).save(existingEntity);
    }

    @Test
    void updatePost_shouldDefaultPostStatusToDraft_whenUpdatedPostPostStatusIsNull() {
        // Arrange
        Post updatedPost = new Post();
        updatedPost.setTitle("Updated Title");
        updatedPost.setContent("Updated Content");
        updatedPost.setPostStatus(DRAFT);

        PostEntity existingEntity = new PostEntity();
        existingEntity.setId(id);
        existingEntity.setTitle("Old Title");
        existingEntity.setContent("Old Content");
        existingEntity.setPostStatus(PostStatus.PUBLISHED);
        existingEntity.setCreatedAt(LocalDateTime.now().minusDays(1));
        existingEntity.setUpdatedAt(LocalDateTime.now().minusDays(1));

        PostEntity savedEntity = new PostEntity();
        savedEntity.setId(id);
        savedEntity.setTitle("Updated Title");
        savedEntity.setContent("Updated Content");
        savedEntity.setPostStatus(DRAFT);
        savedEntity.setCreatedAt(existingEntity.getCreatedAt());
        savedEntity.setUpdatedAt(LocalDateTime.now());

        when(postRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(postRepository.save(any(PostEntity.class))).thenReturn(savedEntity);
        when(postMapper.toPost(savedEntity)).thenReturn(updatedPost);

        // Act
        Post result = postService.updatePost(id, updatedPost);

        // Assert
        assertThat(result.getPostStatus()).isEqualTo(DRAFT);
        verify(postRepository, times(1)).findById(id);
    }


    @Test
    void deletePost_shouldCallRepositoryDeleteById() {
        // Act
        postService.deletePost(id);

        // Assert
        verify(postRepository, times(1)).deleteById(id);
    }

    @Test
    void findPostEntityById_shouldReturnEntity_whenFound() {
        // Arrange
        when(postRepository.findById(id)).thenReturn(Optional.of(postEntity));

        // Act
        PostEntity result = postService.findPostEntityById(id);

        // Assert
        assertThat(result).isEqualTo(postEntity);
        verify(postRepository, times(1)).findById(id);
    }

    @Test
    void findPostEntityById_shouldThrowException_whenNotFound() {
        // Arrange
        when(postRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class, () -> postService.findPostEntityById(id));
        verify(postRepository, times(1)).findById(id);
    }

}
