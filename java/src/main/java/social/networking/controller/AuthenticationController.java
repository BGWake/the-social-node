package social.networking.controller;

import social.networking.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import social.networking.service.UserService;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User newUser) {
        return userService.register(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@Valid @RequestBody User user) {
        return userService.logIn(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(@Valid @RequestBody String username) {
        return userService.logOut(username);
    }

    @DeleteMapping("/user/all")
    public ResponseEntity<?> deleteUsers() {
        return userService.deleteUsers();
    }
}
