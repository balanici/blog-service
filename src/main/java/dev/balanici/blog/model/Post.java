package dev.balanici.blog.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class Post {
    private UUID id;
    private String title;
    private String content;
    //    private Author author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private PostStatus postStatus;

    //    private String category;
    private List<String> tags;
    private String comments;
    private Integer likes;
    private Integer dislikes;
    private Integer shares;
    private Integer views;
//    private String imageUrl;
//    private String videoUrl;
//    private String audioUrl;
}
