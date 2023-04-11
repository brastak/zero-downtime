package codes.bespoke.brastak.snippets.zero.model;

import codes.bespoke.brastak.snippets.zero.mapper.Default;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

public class Post {
    @Id
    private final Long id;
    private final String author;
    private String subject;
    private String text;
    private boolean published;

    @PersistenceCreator
    public Post(Long id, String author, String subject, String text, boolean published) {
        this.id = id;
        this.author = author;
        this.subject = subject;
        this.text = text;
        this.published = published;
    }

    @Default
    public Post(String author, String subject, String text) {
        this(null, author, subject, text, true);
    }

    public Post withId(long id) {
        return new Post(id, this.author, this.subject, this.text, this.published);
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

    public boolean isPublished() {
        return published;
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

    @Override
    public String toString() {
        return "Post{" +
            "id=" + id +
            ", author='" + author + '\'' +
            ", subject='" + subject + '\'' +
            ", text='" + text + '\'' +
            ", published=" + published +
            '}';
    }
}
