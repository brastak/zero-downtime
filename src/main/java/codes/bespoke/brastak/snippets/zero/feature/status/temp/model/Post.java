package codes.bespoke.brastak.snippets.zero.feature.status.temp.model;

import codes.bespoke.brastak.snippets.zero.config.Default;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;


@Table("post")
public class Post {
    @Id
    private final Long id;
    private final String author;
    private String subject;
    private String text;
    private boolean published;
    private Status status;

    @PersistenceCreator
    public Post(Long id, String author, String subject, String text, boolean published, Status status) {
        this.id = id;
        this.author = author;
        this.subject = subject;
        this.text = text;
        this.published = published;
        this.status = status;
    }

    @Default
    public Post(String author, String subject, String text) {
        this(null, author, subject, text, true, Status.PUBLISHED);
    }

    public Post withId(long id) {
        return new Post(id, this.author, this.subject, this.text, this.published, this.status);
    }

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public boolean isPublished() {
        return published;
    }

    public Status getStatus() {
        return status;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatusAwarePost{" +
            "id=" + id +
            ", author='" + author + '\'' +
            ", subject='" + subject + '\'' +
            ", text='" + text + '\'' +
            ", published=" + published +
            ", status=" + status +
            '}';
    }
}
