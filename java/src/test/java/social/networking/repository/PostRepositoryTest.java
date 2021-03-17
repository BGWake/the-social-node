package social.networking.repository;

import social.networking.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    private Post post;

    @BeforeEach
    void setUp() {
        post = new Post("test", "test");
        postRepository.deleteAll();
    }

    @Test
    void repository_persistence_works() {
        Post savedPost = postRepository.save(post);

        Post foundPost = postRepository.findById(savedPost.getId()).orElse(null);

        assertEquals(post, foundPost);
    }

    @Test
    void find_posts_by_username_is_successful() {
        postRepository.save(post);

        List<Post> foundPosts = postRepository.findPostsByUsername("test");

        Post foundPost = foundPosts.get(0);

        assertEquals(post, foundPost);
    }
}