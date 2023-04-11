package codes.bespoke.brastak.snippets.zero.mapper;

import codes.bespoke.brastak.snippets.zero.dto.CreatePostRequestDto;
import codes.bespoke.brastak.snippets.zero.dto.PostDto;
import codes.bespoke.brastak.snippets.zero.model.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post createPostRequestDtoToPost(CreatePostRequestDto dto);

    PostDto postToPostDto(Post post);
}
