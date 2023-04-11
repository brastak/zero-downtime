package codes.bespoke.brastak.snippets.zero.service;

import java.util.Optional;

import codes.bespoke.brastak.snippets.zero.dto.CreatePostRequestDto;
import codes.bespoke.brastak.snippets.zero.dto.PostDto;
import codes.bespoke.brastak.snippets.zero.mapper.PostMapper;
import codes.bespoke.brastak.snippets.zero.model.Post;
import codes.bespoke.brastak.snippets.zero.repository.PostRepository;

public class WriteOperationsImpl implements WriteOperations {
    private final PostRepository repository;
    private final PostMapper mapper;

    public WriteOperationsImpl(PostRepository repository, PostMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PostDto create(CreatePostRequestDto createPostRequest) {
        Post post = mapper.createPostRequestDtoToPost(createPostRequest);
        return mapper.postToPostDto(repository.save(post));
    }

    @Override
    public Optional<Long> lock(long id) {
        return repository.lock(id);
    }

    @Override
    public void hide(long id) {
        repository.hidePost(id);
    }
}
