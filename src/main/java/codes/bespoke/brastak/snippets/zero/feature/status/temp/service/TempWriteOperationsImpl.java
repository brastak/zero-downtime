package codes.bespoke.brastak.snippets.zero.feature.status.temp.service;

import java.util.Optional;

import codes.bespoke.brastak.snippets.zero.dto.CreatePostRequestDto;
import codes.bespoke.brastak.snippets.zero.dto.PostDto;
import codes.bespoke.brastak.snippets.zero.feature.status.temp.mapper.TempPostMapper;
import codes.bespoke.brastak.snippets.zero.feature.status.temp.model.Post;
import codes.bespoke.brastak.snippets.zero.feature.status.temp.model.Status;
import codes.bespoke.brastak.snippets.zero.feature.status.temp.repository.TempPostRepository;
import codes.bespoke.brastak.snippets.zero.service.WriteOperations;

public class TempWriteOperationsImpl implements WriteOperations {
    private final TempPostRepository repository;
    private final TempPostMapper mapper;

    public TempWriteOperationsImpl(TempPostRepository repository, TempPostMapper mapper) {
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
        repository.hidePost(id, Status.UNPUBLISHED);
    }
}
