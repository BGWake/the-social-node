package social.networking.repository;

import social.networking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    User findUserByLoggedInIsTrue();

    User findFollowingByUsername(String username);
}
