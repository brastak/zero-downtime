package codes.bespoke.brastak.snippets.zero.feature.status.old.repository;

import java.util.List;
import java.util.Optional;

import codes.bespoke.brastak.snippets.zero.feature.status.old.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OldPostRepository extends PagingAndSortingRepository<Post, Long>, CrudRepository<Post, Long> {
    List<Post> findByAuthorAndPublishedIsTrue(String author, Pageable pageable);

    List<Post> findByAuthor(String author, Pageable pageable);

    @Transactional(propagation = Propagation.MANDATORY)
    @Query(value = "select id from post where id = :id for update")
    Optional<Long> lock(long id);

    @Modifying
    @Transactional(propagation = Propagation.MANDATORY)
    @Query(value = "update post set published = false where id = :id")
    int hidePost(long id);
}
