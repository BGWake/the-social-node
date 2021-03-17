package social.networking.controller;

import social.networking.model.User;
import social.networking.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
public class AuthenticationController {

    private final UserRepository userRepository;

    public AuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User newUser) {
        List<User> users = userRepository.findAll();
        System.out.println("New user: " + newUser.toString());
        for (User user : users) {
            System.out.println("Registered user: " + newUser.toString());
            if (user.equals(newUser)) {
                System.out.println("User Already exists!");
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        userRepository.save(newUser);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user) {

        User loggedInUser = userRepository.findUserByLoggedInIsTrue();

        if (loggedInUser != null) {
            loggedInUser.setLoggedIn(false);
            userRepository.save(loggedInUser);
        }

        List<User> users = userRepository.findAll();
        for (User other : users) {

            if (other.equals(user)) {
                other.setLoggedIn(true);
                userRepository.save(other);
                return new ResponseEntity<>(null, HttpStatus.CREATED);

            }
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(@Valid @RequestBody String username) {

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

    @DeleteMapping("/user/all")
    public ResponseEntity<?> deleteUsers() {
        userRepository.deleteAll();
        return new ResponseEntity<>(null, HttpStatus.OK);

    }
}
