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

        return findTagsInPostsAndAddToRelevantPosts(yourRelevantPosts, username);
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

        for (String followedUser : followingArray) {
            List<Post> followedUserPosts = postRepository.findPostsByUsername(followedUser);
            yourRelevantPosts.addAll(followedUserPosts);
        }

        return findTagsInPostsAndAddToRelevantPosts(yourRelevantPosts, username);
    }

    public List<Post> findTagsInPostsAndAddToRelevantPosts(List<Post> yourRelevantPosts, String username) {
        List<Post> allPosts = postRepository.findAll();

        for (Post post : allPosts) {

            StringBuilder taggedUser = new StringBuilder();
            boolean tagFlag = false;

            for (int i = 0; i < post.getContent().length(); i++) {
                if (post.getContent().charAt(i) == ' ' || post.getContent().charAt(i) == '.' ||
                        post.getContent().charAt(i) == '!' || post.getContent().charAt(i) == '?' ||
                        post.getContent().charAt(i) == ',') {
                    tagFlag = false;
                    taggedUser = new StringBuilder();
                }
                if (tagFlag) {
                    taggedUser.append(post.getContent().charAt(i));

                    if (taggedUser.toString().equals(username) && !yourRelevantPosts.contains(post)) {
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

