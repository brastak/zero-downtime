package codes.bespoke.brastak.snippets.zero.feature.status.target.service;

import java.util.List;
import java.util.Optional;

import codes.bespoke.brastak.snippets.zero.dto.PostDto;
import codes.bespoke.brastak.snippets.zero.feature.status.target.mapper.PostMapper;
import codes.bespoke.brastak.snippets.zero.feature.status.target.model.Post;
import codes.bespoke.brastak.snippets.zero.feature.status.target.model.Status;
import codes.bespoke.brastak.snippets.zero.feature.status.target.repository.PostRepository;
import codes.bespoke.brastak.snippets.zero.service.ReadOperations;
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
            posts = repository.findPostByAuthorAndStatus(
                author, Status.PUBLISHED, PageRequest.of(0, 20, Sort.by("id").descending())
            );
        } else {
            posts = repository.findPostByAuthor(
                author, PageRequest.of(0, 20, Sort.by("id").descending())
            );
        }
        return posts.stream().map(mapper::postToPostDto).toList();
    }
}
