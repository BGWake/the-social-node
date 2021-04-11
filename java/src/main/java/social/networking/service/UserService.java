package social.networking.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import social.networking.model.User;
import social.networking.repository.UserRepository;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> register(User newUser) {
        List<User> users = userRepository.findAll();
        boolean userAlreadyExists = false;

        for (User user : users) {
            if (user.getUsername().equals(newUser.getUsername())) {
                userAlreadyExists = true;
                break;
            }
        }
        if (userAlreadyExists) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            BCryptPasswordEncoder bCryptPasswordEncoder =
                    new BCryptPasswordEncoder(4, new SecureRandom());
            String encodedPassword = bCryptPasswordEncoder.encode(newUser.getPassword());

            newUser.setPassword(encodedPassword);

            if (newUser.getProfilePic() == "") {
                newUser.setProfilePic
                        ("https://www.nicepng.com/png/full/115-1150821_default-avatar-comments-sign-in-icon-png.png");
            }

            userRepository.save(newUser);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        }
    }

    public ResponseEntity<?> logIn(User user) {
        User loggedInUser = userRepository.findUserByLoggedInIsTrue();

        if (loggedInUser != null) {
            loggedInUser.setLoggedIn(false);
            userRepository.save(loggedInUser);
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        List<User> users = userRepository.findAll();
        for (User other : users) {

            if (other.getUsername().equals(user.getUsername()) &&
                    bCryptPasswordEncoder.matches(user.getPassword(), other.getPassword())) {

                other.setLoggedIn(true);
                userRepository.save(other);
                return new ResponseEntity<>(null, HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> logOut(String username) {
        List<User> users = userRepository.findAll();
        for (User other : users) {

            if ((other.getUsername() + "=").equals(username)) {
                other.setLoggedIn(false);
                userRepository.save(other);
                return new ResponseEntity<>(null, HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> deleteUsers() {
        userRepository.deleteAll();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public List<String> getAllUsernames() {
        List<User> users = userRepository.findAll();
        List<String> usernames = new ArrayList<>();

        for (User user : users) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }

    public void addToFollowing(User user) {
        List<User> users = userRepository.findAll();

        for (User other : users) {

            if (other.getUsername().equals(user.getUsername())
                    && !other.getFollowing().contains(user.getFollowing() + ",")) {
                if (other.getFollowing().equals("")) {
                    other.setFollowing(user.getFollowing() + ",");
                } else {
                    other.setFollowing(other.getFollowing() + user.getFollowing() + ",");
                }
                userRepository.save(other);
            }
        }
    }

    public void removeFromFollowing(User user) {
        List<User> users = userRepository.findAll();

        for (User other : users) {

            if (other.getUsername().equals(user.getUsername())
                    && other.getFollowing().contains(user.getFollowing() + ",")) {
                    other.setFollowing(other.getFollowing().replaceAll((user.getFollowing() + ","), ""));
            }
            userRepository.save(other);
        }
    }

    public String getFollowing(String username) {
        return userRepository.findFollowingByUsername(username).getFollowing();
    }
}
