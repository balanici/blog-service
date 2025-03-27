package dev.balanici.blog.model;

public enum PostStatus {
    DRAFT("DRAFT"),
    PUBLISHED("PUBLISHED"),
    ARCHIVED("ARCHIVED"),
    DELETED("DELETED");


    PostStatus(String value) {
    }

}
