package codes.bespoke.brastak.snippets.zero.feature.status.target.model;

import codes.bespoke.brastak.snippets.zero.config.Default;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

public class Post {
    @Id
    private final Long id;
    private final String author;
    private String subject;
    private String text;
    private Status status;

    @PersistenceCreator
    public Post(Long id, String author, String subject, String text, Status status) {
        this.id = id;
        this.author = author;
        this.subject = subject;
        this.text = text;
        this.status = status;
    }

    @Default
    public Post(String author, String subject, String text) {
        this(null, author, subject, text, Status.PUBLISHED);
    }

    public Post withId(long id) {
        return new Post(id, this.author, this.subject, this.text, this.status);
    }

    public long getId() {
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

    public Status getStatus() {
        return status;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Post{" +
            "id=" + id +
            ", author='" + author + '\'' +
            ", subject='" + subject + '\'' +
            ", text='" + text + '\'' +
            ", status=" + status +
            '}';
    }
}
