package dev.balanici.blog.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class Post {
    private UUID id;
    @NotNull
    private String title;
    @NotNull
    private String content;

    private PostStatus postStatus;

    private List<Comment> comments;

    private Integer views;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
