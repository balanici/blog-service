package dev.balanici.blog.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Post {
    private UUID id;
    @NotNull
    private String title;
    @NotNull
    private String content;

    private PostStatus postStatus;

    private Integer views;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
