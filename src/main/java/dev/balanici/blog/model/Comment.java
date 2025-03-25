package dev.balanici.blog.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Comment {
    private UUID id;
    @NotNull
    private String content;
    @NotNull
    private UUID postId;
//    private UUID userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
