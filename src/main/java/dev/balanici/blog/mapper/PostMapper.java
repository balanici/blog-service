package dev.balanici.blog.mapper;

import dev.balanici.blog.entity.PostEntity;
import dev.balanici.blog.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPost(PostEntity postEntity);

//    @Mapping(source = "postStatus", target = "postStatus")
    PostEntity toPostEntity(Post post);
}
