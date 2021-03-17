package social.networking.controller;

import social.networking.model.Post;
import social.networking.model.User;
import social.networking.repository.PostRepository;
import social.networking.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
public class SocialNetworkingController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public SocialNetworkingController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/post/all")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/post/{username}")
    public List<Post> getPostsByUsername(@PathVariable String username) {
        List<Post> allPosts = postRepository.findAll();

        List<Post> relevantPosts = new ArrayList<>();

        for (Post post : allPosts) {

            String taggedUser = "";
            boolean tagFlag = false;

            for (int i = 0; i < post.getContent().length(); i++) {
                if (post.getContent().charAt(i) == ' ') {
                    tagFlag = false;
                    taggedUser = "";
                }
                if (tagFlag) {
                    taggedUser += post.getContent().charAt(i);

                    if (taggedUser.equals(username)) {
                        relevantPosts.add(post);
                    }
                }
                if (post.getContent().charAt(i) == '@') {
                    tagFlag = true;
                }
            }
        }

        relevantPosts.addAll(postRepository.findPostsByUsername(username));
        relevantPosts.sort(Comparator.comparing(Post::getTime));

        return relevantPosts;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/post/save")
    public void createPost(@RequestBody Post post) {
        post.setTime(LocalDateTime.now());

        postRepository.save(post);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userRepository.findUserByUsername(username);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/all")
    public List<String> findUsernames() {
        List<User> users = userRepository.findAll();

        List<String> usernames = new ArrayList<>();

        for (User user : users) {
            usernames.add(user.getUsername());
        }
        System.out.println(usernames);
        return usernames;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/follow")
    public void addToFollowing(@RequestBody User user) {
        List<User> users = userRepository.findAll();

        for (User other : users) {

            if (other.getUsername().equals(user.getUsername())
                    && !other.getFollowing().contains(user.getFollowing())) {
                if (!other.getFollowing().equals("")) {
                    other.setFollowing(other.getFollowing() + "," + user.getFollowing());
                } else {
                    other.setFollowing(user.getFollowing());
                }
                userRepository.save(other);
            }
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/feed/posts/{username}")
    public List<Post> getAllRelevantPostsForYourFeed(@PathVariable String username) {

        List<Post> yourRelevantPosts = postRepository.findPostsByUsername(username);

        User user = userRepository.findUserByUsername(username);

        String following = user.getFollowing();
        String[] followingArray = following.split(",");
        System.out.println("Contents of the array ::" + Arrays.toString(followingArray));

        for (String followedUser : followingArray) {
            List<Post> followedUserPosts = postRepository.findPostsByUsername(followedUser);
            yourRelevantPosts.addAll(followedUserPosts);
        }

        List<Post> allPosts = postRepository.findAll();

        for (Post post : allPosts) {

            String taggedUser = "";
            boolean tagFlag = false;

            for (int i = 0; i < post.getContent().length(); i++) {
                if (post.getContent().charAt(i) == ' ') {
                    tagFlag = false;
                    taggedUser = "";
                }
                if (tagFlag) {
                    taggedUser += post.getContent().charAt(i);

                    if (taggedUser.equals(username) && !yourRelevantPosts.contains(post)) {
                        yourRelevantPosts.add(post);
                    }
                }
                if (post.getContent().charAt(i) == '@') {
                    tagFlag = true;
                }
            }
        }

        yourRelevantPosts.sort(Comparator.comparing(Post::getTime));

        return yourRelevantPosts;
    }

}
