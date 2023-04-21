package codes.bespoke.brastak.snippets.zero.feature.status.temp.mapper;

import codes.bespoke.brastak.snippets.zero.dto.CreatePostRequestDto;
import codes.bespoke.brastak.snippets.zero.dto.PostDto;
import codes.bespoke.brastak.snippets.zero.feature.status.temp.model.Status;
import codes.bespoke.brastak.snippets.zero.feature.status.temp.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TempPostMapper {
    Post createPostRequestDtoToPost(CreatePostRequestDto dto);

    @Mapping(target = "published", expression = "java(isPublished(post))")
    PostDto postToPostDto(Post post);

    default boolean isPublished(Post post) {
        return post.getStatus() == Status.PUBLISHED;
    }
}
