package codes.bespoke.brastak.snippets.zero.feature.status.old.mapper;

import codes.bespoke.brastak.snippets.zero.dto.CreatePostRequestDto;
import codes.bespoke.brastak.snippets.zero.dto.PostDto;
import codes.bespoke.brastak.snippets.zero.feature.status.old.model.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OldPostMapper {
    Post createPostRequestDtoToPost(CreatePostRequestDto dto);

    PostDto postToPostDto(Post post);
}
