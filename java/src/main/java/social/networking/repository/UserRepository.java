package social.networking.repository;

import social.networking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    User findUserByLoggedInIsTrue();

    List<User> findAll();

}
