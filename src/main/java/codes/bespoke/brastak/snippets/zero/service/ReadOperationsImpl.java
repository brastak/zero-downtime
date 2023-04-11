package codes.bespoke.brastak.snippets.zero.service;

import java.util.List;
import java.util.Optional;

import codes.bespoke.brastak.snippets.zero.dto.PostDto;
import codes.bespoke.brastak.snippets.zero.mapper.PostMapper;
import codes.bespoke.brastak.snippets.zero.model.Post;
import codes.bespoke.brastak.snippets.zero.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class ReadOperationsImpl implements ReadOperations {
    private final PostRepository repository;
    private final PostMapper mapper;

    public ReadOperationsImpl(PostRepository repository, PostMapper mapper) {
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
            posts = repository.findPostByAuthorAndPublishedIsTrue(
                author, PageRequest.of(0, 20, Sort.by("id").descending())
            );
        } else {
            posts = repository.findPostByAuthor(
                author, PageRequest.of(0, 20, Sort.by("id").descending())
            );
        }
        return posts.stream().map(mapper::postToPostDto).toList();
    }
}
