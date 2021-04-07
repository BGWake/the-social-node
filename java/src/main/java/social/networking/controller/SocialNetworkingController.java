package social.networking.controller;

import com.fasterxml.jackson.annotation.JsonView;
import social.networking.model.Post;
import social.networking.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import social.networking.model.UserView;
import social.networking.repository.UserRepository;
import social.networking.service.PostService;
import social.networking.service.UserService;

import java.util.List;

@CrossOrigin
@RestController
public class SocialNetworkingController {

    private final PostService postService;
    private final UserService userService;
    private final UserRepository userRepository;

    public SocialNetworkingController(PostService postService, UserService userService, UserRepository userRepository) {
        this.postService = postService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/post/all")
    public List<Post> getAllPosts() {
        return postService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/post/{username}")
    public List<Post> getPostsByUsername(@PathVariable String username) {
        return postService.getPostsByUsername(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/post/save")
    public void createPost(@RequestBody Post post) {
        postService.create(post);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/feed/posts/{username}")
    public List<Post> getAllRelevantPostsForYourFeed(@PathVariable String username) {
        return postService.getAllRelevantToYourFeed(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/post/all/save")
    public void updatePosts(@RequestBody List<Post> posts) { postService.updateRelevantPosts(posts); }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/all")
    @JsonView(UserView.Public.class)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/follow")
    public void addToFollowing(@RequestBody User user) {
        userService.addToFollowing(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/following/{username}")
    public String getFollowingByUsername(@PathVariable String username) {
        return userService.getFollowing(username);
    }
}
