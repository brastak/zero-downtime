package codes.bespoke.brastak.snippets.zero.feature.status.target.repository;

import java.util.List;
import java.util.Optional;

import codes.bespoke.brastak.snippets.zero.feature.status.target.model.Post;
import codes.bespoke.brastak.snippets.zero.feature.status.target.model.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long>, CrudRepository<Post, Long> {
    @Override
    @Query("insert into post (subject, text, author, status)" +
        " values (:#{#post.subject}, :#{#post.text}, :#{#post.author}, :#{#post.status.name()})" +
        " returning id, subject, text, author, status")
    <S extends Post> S save(S post);

    List<Post> findPostByAuthorAndStatus(String author, Status status, Pageable pageable);

    List<Post> findPostByAuthor(String author, Pageable pageable);

    @Transactional(propagation = Propagation.MANDATORY)
    @Query(value = "select id from post where id = :id for update")
    Optional<Long> lock(long id);

    @Modifying
    @Transactional(propagation = Propagation.MANDATORY)
    @Query(value = "update post set status = :status where id = :id")
    int hidePost(long id, Status status);
}
