package social.networking.service;

import org.springframework.stereotype.Service;
import social.networking.model.Post;
import social.networking.model.User;
import social.networking.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByUsername(String username) {

        List<Post> yourRelevantPosts = new ArrayList<>();

        yourRelevantPosts.addAll(postRepository.findPostsByUsername(username));

        List<Post> yourRelevantPostsFinal = findTagsInPosts(yourRelevantPosts, username);

        return yourRelevantPostsFinal;
    }

    public void create(Post post) {
        post.setTime(LocalDateTime.now());

        postRepository.save(post);
    }

    public List<Post> getAllRelevantToYourFeed(String username) {

        List<Post> yourRelevantPosts = postRepository.findPostsByUsername(username);

        User user = userService.findUserByUsername(username);

        String following = user.getFollowing();
        String[] followingArray = following.split(",");
        System.out.println("Contents of the array ::" + Arrays.toString(followingArray));

        for (String followedUser : followingArray) {
            List<Post> followedUserPosts = postRepository.findPostsByUsername(followedUser);
            yourRelevantPosts.addAll(followedUserPosts);
        }

        List<Post> yourRelevantPostsFinal = findTagsInPosts(yourRelevantPosts, username);

        return yourRelevantPostsFinal;
    }

    public List<Post> findTagsInPosts(List<Post> yourRelevantPosts, String username) {
        List<Post> allPosts = postRepository.findAll();

        for (Post post : allPosts) {

            String taggedUser = "";
            boolean tagFlag = false;

            for (int i = 0; i < post.getContent().length(); i++) {
                if (post.getContent().charAt(i) == ' ' || post.getContent().charAt(i) == '.' ||
                        post.getContent().charAt(i) == '!' || post.getContent().charAt(i) == '?' ||
                        post.getContent().charAt(i) == ',') {
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

