package dev.balanici.blog.model;

public enum PostStatus {
    DRAFT("DRAFT"),
    PUBLISHED("PUBLISHED"),
    ARCHIVED("ARCHIVED"),
    DELETED("DELETED");

    private final String value;


    PostStatus(String value) {
        this.value = value;
    }
}
