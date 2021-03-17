package social.networking;

import social.networking.model.Post;
import social.networking.model.User;
import social.networking.repository.PostRepository;
import social.networking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Populator implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public void run(String... args) {
        User user = new User("bob", "password", "");
        User user2 = new User("steve", "password", "");
        User user3 = new User("jim", "password", "");

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        Post post = new Post("bob", "Had a good day.");
        Post post2 = new Post("bob", "Walked the dog today!");
        Post post3 = new Post("steve", "I wonder if it'll snow?");
        Post post4 = new Post("steve", "It's snowing!");
        Post post5 = new Post("jim", "It's my birthday!");
        Post post6 = new Post("jim", "Come over at 7 for the party.");

        postRepository.save(post);
        post2.setTime(LocalDateTime.now().plusSeconds(1));
        postRepository.save(post2);
        post3.setTime(LocalDateTime.now().plusSeconds(2));
        postRepository.save(post3);
        post4.setTime(LocalDateTime.now().plusSeconds(3));
        postRepository.save(post4);
        post5.setTime(LocalDateTime.now().plusSeconds(4));
        postRepository.save(post5);
        post6.setTime(LocalDateTime.now().plusSeconds(5));
        postRepository.save(post6);
    }
}
