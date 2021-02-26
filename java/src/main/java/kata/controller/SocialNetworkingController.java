package kata.controller;

import kata.model.Post;
import kata.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SocialNetworkingController {

    private final PostRepository postRepository;

    public SocialNetworkingController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/allPosts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/posts/{username}")
    public List<Post> getPostsByUsername(@PathVariable String username) {
        return postRepository.findPostsByUsername(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/post/save")
    public void createPost(@RequestBody Post post) {
        postRepository.save(post);
    }
}
