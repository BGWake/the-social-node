package social.networking;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import social.networking.model.Post;
import social.networking.model.User;
import social.networking.repository.PostRepository;
import social.networking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Component
public class Populator implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Populator(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    BCryptPasswordEncoder bCryptPasswordEncoder =
            new BCryptPasswordEncoder(6, new SecureRandom());

    @Override
    public void run(String... args) {
        User user = new User("bob", "password", "", "https://www.nicepng.com/png/full/115-1150821_default-avatar-comments-sign-in-icon-png.png");
        User user2 = new User("tom", "password", "", "https://pbs.twimg.com/profile_images/1237550450/mstom_400x400.jpg");
        User user3 = new User("anne", "password", "", "https://www.nicepng.com/png/full/115-1150821_default-avatar-comments-sign-in-icon-png.png");
        User user4 = new User("jim", "password", "", "https://www.nicepng.com/png/full/115-1150821_default-avatar-comments-sign-in-icon-png.png");
        User user5 = new User("dog", "password", "", "https://images.pexels.com/photos/1078090/pexels-photo-1078090.jpeg");

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        String encodedPassword2 = bCryptPasswordEncoder.encode(user2.getPassword());
        user2.setPassword(encodedPassword2);
        String encodedPassword3 = bCryptPasswordEncoder.encode(user3.getPassword());
        user3.setPassword(encodedPassword3);
        String encodedPassword4 = bCryptPasswordEncoder.encode(user4.getPassword());
        user4.setPassword(encodedPassword4);
        String encodedPassword5 = bCryptPasswordEncoder.encode(user5.getPassword());
        user5.setPassword(encodedPassword5);

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);

        Post post = new Post("bob", "Had a good day.", "tom");
        Post post2 = new Post("bob", "Walked the dog today!", "anne, dog");
        Post post3 = new Post("tom", "I wonder if it'll rain?", "bob, jim");
        Post post4 = new Post("tom", "It's raining!", "anne");
        Post post5 = new Post("anne", "Rode my bike on my favorite trail today.", "bob");
        Post post6 = new Post("anne", "Tag me if you know of any good ones in the area!", "jim");
        Post post7 = new Post("jim", "Hey @anne, there's a great trail 15 miles west of town.", "anne");
        Post post8 = new Post("jim", "It's my birthday!", "bob");
        Post post9 = new Post("jim", "Come over at 7 for the party.", "bob, tom");
        Post post10 = new Post("dog", "woof", "bob");

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
        post7.setTime(LocalDateTime.now().plusSeconds(6));
        postRepository.save(post7);
        post8.setTime(LocalDateTime.now().plusSeconds(7));
        postRepository.save(post8);
        post9.setTime(LocalDateTime.now().plusSeconds(8));
        postRepository.save(post9);
        post10.setTime(LocalDateTime.now().plusSeconds(9));
        postRepository.save(post10);
    }
}
