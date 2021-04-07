package social.networking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String content;
    private String time;
    private String likes;

    public Post(String username, String content, String likes) {
        this.username = username;
        this.content = content;
        this.time = LocalDateTime.now().toString();
        this.likes = likes;
    }

    public Post() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time.toString();
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return username.equals(post.username) && content.equals(post.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, content);
    }
}
