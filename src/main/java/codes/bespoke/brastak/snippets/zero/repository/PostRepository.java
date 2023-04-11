package codes.bespoke.brastak.snippets.zero.repository;

import java.util.List;
import java.util.Optional;

import codes.bespoke.brastak.snippets.zero.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long>, CrudRepository<Post, Long> {
    @Override
    @Query("insert into post (subject, text, author, published)" +
        " values (:#{#post.subject}, :#{#post.text}, :#{#post.author}, :#{#post.published})" +
        " returning id, subject, text, author, published")
    <S extends Post> S save(S post);

    List<Post> findPostByAuthorAndPublishedIsTrue(String author, Pageable pageable);

    List<Post> findPostByAuthor(String author, Pageable pageable);

    @Transactional(propagation = Propagation.MANDATORY)
    @Query(value = "select id from post where id = :id for update")
    Optional<Long> lock(long id);

    @Modifying
    @Transactional(propagation = Propagation.MANDATORY)
    @Query(value = "update post set published = false where id = :id")
    int hidePost(long id);
}
