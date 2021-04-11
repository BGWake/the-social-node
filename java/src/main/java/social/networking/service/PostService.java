package social.networking.service;

import org.springframework.stereotype.Service;
import social.networking.model.Post;
import social.networking.model.User;
import social.networking.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        List<Post> yourRelevantPosts = new ArrayList<>(postRepository.findPostsByUsername(username));

        return findTagsInPostsAndSharedPostsThenAddToRelevantPosts(yourRelevantPosts, username);
    }

    public void create(Post post) {
        if (post.getTime().equals("")) {
            post.setTime(LocalDateTime.now().minusHours(4));
        }
        postRepository.save(post);
    }

    public void updateRelevantPosts(List<Post> posts) {

        for (Post post : posts) {
            postRepository.save(post);
        }
    }

    public List<Post> getAllRelevantToYourFeed(String username) {

        List<Post> yourRelevantPosts = postRepository.findPostsByUsername(username);

        User user = userService.findUserByUsername(username);

        String following = user.getFollowing();
        String[] followingArray = following.split(",");

        for (String followedUser : followingArray) {
            List<Post> followedUserPosts = postRepository.findPostsByUsername(followedUser);
            yourRelevantPosts.addAll(followedUserPosts);
        }

        return findTagsInPostsAndSharedPostsThenAddToRelevantPosts(yourRelevantPosts, username);
    }

    private List<Post> findTagsInPostsAndSharedPostsThenAddToRelevantPosts(List<Post> yourRelevantPosts, String username) {
        List<Post> allPosts = postRepository.findAll();

        for (Post post : allPosts) {

            if ((post.getContent().contains("@" + username + " ") || post.getContent().contains("@" + username + ".") ||
                    post.getContent().contains("@" + username + "!") ||
                    post.getContent().contains("@" + username + "?") ||
                    post.getContent().contains("@" + username + ",")) || post.getShared().contains(username + ",")
                    && !yourRelevantPosts.contains(post)) {

                yourRelevantPosts.add(post);
            }
            if (post.getContent().endsWith("@" + username) &&
                    !yourRelevantPosts.contains(post)) {

                    yourRelevantPosts.add(post);
                }
            }
        yourRelevantPosts.sort(Comparator.comparing(Post::getTime));

        return yourRelevantPosts;
    }
}
