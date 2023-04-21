package codes.bespoke.brastak.snippets.zero.feature.status.old.service;

import java.util.List;
import java.util.Optional;

import codes.bespoke.brastak.snippets.zero.dto.PostDto;
import codes.bespoke.brastak.snippets.zero.feature.status.old.mapper.OldPostMapper;
import codes.bespoke.brastak.snippets.zero.feature.status.old.model.Post;
import codes.bespoke.brastak.snippets.zero.feature.status.old.repository.OldPostRepository;
import codes.bespoke.brastak.snippets.zero.service.ReadOperations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class OldReadOperationsImpl implements ReadOperations {
    private final OldPostRepository repository;
    private final OldPostMapper mapper;

    public OldReadOperationsImpl(OldPostRepository repository, OldPostMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<PostDto> findById(long id) {
        return repository.findById(id).map(mapper::postToPostDto);
    }

    @Override
    public List<PostDto> findByAuthor(String author, boolean publishedOnly) {
        List<Post> posts;
        if(publishedOnly) {
            posts = repository.findByAuthorAndPublishedIsTrue(
                author, PageRequest.of(0, 20, Sort.by("id").descending())
            );
        } else {
            posts = repository.findByAuthor(
                author, PageRequest.of(0, 20, Sort.by("id").descending())
            );
        }
        return posts.stream().map(mapper::postToPostDto).toList();
    }
}
