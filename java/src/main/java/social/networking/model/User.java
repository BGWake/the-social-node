package social.networking.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue
    @JsonView(UserView.Public.class)
    private Long id;
    @NotNull
    @JsonView(UserView.Public.class)
    private String username;
    @NotNull
    @JsonView(UserView.Private.class)
    private String password;
    @NotNull
    @JsonView(UserView.Public.class)
    private boolean loggedIn;
    @JsonView(UserView.Public.class)
    private String following;
    @JsonView(UserView.Public.class)
    private String profilePic;

    public User(String username, String password, String following, String profilePic) {
        this.username = username;
        this.password = password;
        this.loggedIn = false;
        this.following = following;
        this.profilePic = profilePic;
    }

    public User() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password,
                loggedIn);
    }
}
