package grooteogi.repository;

import grooteogi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u join fetch u.authorities a where u.email = :email")
    Optional<User> findByEmailWithAuthority(String email);

    Optional<User> findByEmail( String email );
}
