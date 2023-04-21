package codes.bespoke.brastak.snippets.zero.feature.status.temp.repository;

import java.util.Optional;

import codes.bespoke.brastak.snippets.zero.feature.status.temp.model.Status;
import codes.bespoke.brastak.snippets.zero.feature.status.temp.model.Post;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface TempPostRepository extends PagingAndSortingRepository<Post, Long>, CrudRepository<Post, Long> {
    @Override
    @Query("insert into post (subject, text, author, published, status)" +
        " values (:#{#post.subject}, :#{#post.text}, :#{#post.author}, :#{#post.published}, :#{#post.status.name()})" +
        " returning id, subject, text, author, published, status")
    <S extends Post> S save(S post);

    @Transactional(propagation = Propagation.MANDATORY)
    @Query(value = "select id from post where id = :id for update")
    Optional<Long> lock(long id);

    @Modifying
    @Transactional(propagation = Propagation.MANDATORY)
    @Query(value = "update post set status = :status, published = false where id = :id")
    int hidePost(long id, Status status);
}
