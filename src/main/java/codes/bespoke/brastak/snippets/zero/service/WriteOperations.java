package codes.bespoke.brastak.snippets.zero.service;

import java.util.Optional;

import codes.bespoke.brastak.snippets.zero.dto.CreatePostRequestDto;
import codes.bespoke.brastak.snippets.zero.dto.PostDto;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface WriteOperations {
    PostDto create(CreatePostRequestDto createPostRequest);

    @Transactional(propagation = Propagation.MANDATORY)
    Optional<Long> lock(long id);

    @Transactional(propagation = Propagation.MANDATORY)
    void hide(long id);
}
