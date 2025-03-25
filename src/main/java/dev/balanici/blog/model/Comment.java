package dev.balanici.blog.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class Comment {
    private UUID id;
    @NotNull
    private String content;
    @NotNull
    private UUID postId;
//    private UUID userId;

}
