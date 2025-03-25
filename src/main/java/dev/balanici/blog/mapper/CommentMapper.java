package dev.balanici.blog.mapper;

import dev.balanici.blog.entity.CommentEntity;
import dev.balanici.blog.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {


    @Mapping(source = "postEntity.id", target = "postId")
    Comment toComment(CommentEntity commentEntity);

    @Mapping(source = "postId", target = "postEntity.id")
    CommentEntity toCommentEntity(Comment comment);

}
