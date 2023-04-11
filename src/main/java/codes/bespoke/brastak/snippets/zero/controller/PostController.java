package codes.bespoke.brastak.snippets.zero.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import codes.bespoke.brastak.snippets.zero.dto.CreatePostRequestDto;
import codes.bespoke.brastak.snippets.zero.dto.PostDto;
import codes.bespoke.brastak.snippets.zero.service.ReadOperations;
import codes.bespoke.brastak.snippets.zero.service.WriteOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/posts")
public class PostController {
    private static final Logger log = LogManager.getLogger();

    private final ReadOperations readOperations;
    private final WriteOperations writeOperations;

    public PostController(ReadOperations readOperations, WriteOperations writeOperations) {
        this.readOperations = readOperations;
        this.writeOperations = writeOperations;
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostRequestDto createPostRequest) throws URISyntaxException {
        log.debug("Processing create post request {}", createPostRequest);
        PostDto post = writeOperations.create(createPostRequest);
        log.debug("Post successfully created {}", post);
        return ResponseEntity.created(
            new URI(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString() + "/" + post.id())
        ).body(post);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> findById(@PathVariable("id") long id) {
        log.debug("Finding post by id {}", id);
        Optional<PostDto> post = readOperations.findById(id);
        log.debug("Found post by id {}: {}", id, post);
        return ResponseEntity.of(post);
    }

    @GetMapping
    public List<PostDto> findByAuthor(@RequestParam("author") String author,
                                      @RequestParam(value = "publishedOnly", defaultValue = "true") boolean publishedOnly) {
        log.debug("Finding last posts by author {} (publishedOnly={})", author, publishedOnly);
        List<PostDto> posts = readOperations.findByAuthor(author, publishedOnly);
        log.debug("Found {} posts by author {} (publishedOnly={})", posts.size(), author, publishedOnly);
        log.trace("Found last posts by author {} (publishedOnly={}): {}", author, publishedOnly, posts);
        return posts;
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> hidePost(@PathVariable("id") long id) {
        log.debug("Hiding post with id {}", id);
        return writeOperations.lock(id)
            .map(postId -> {
                writeOperations.hide(postId);
                log.debug("Post with id {} successfully hided", postId);
                return ResponseEntity.noContent().build();
            })
            .orElseGet(() -> {
                log.debug("Post with id {} not found to be hided", id);
                return ResponseEntity.notFound().build();
            });
    }
}
