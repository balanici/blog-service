package dev.balanici.blog.mapper;

import dev.balanici.blog.entity.PostEntity;
import dev.balanici.blog.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "comments", target = "comments")
    Post toPost(PostEntity postEntity);

    @Mapping(source = "comments", target = "comments")
    PostEntity toPostEntity(Post post);
}
