package codes.bespoke.brastak.snippets.zero.service;

import java.util.List;
import java.util.Optional;

import codes.bespoke.brastak.snippets.zero.dto.PostDto;

public interface ReadOperations {
    Optional<PostDto> findById(long id);

    List<PostDto> findByAuthor(String author, boolean publishedOnly);
}
