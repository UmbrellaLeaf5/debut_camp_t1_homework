package ru.t1.jwt_assymetric.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
  boolean existsByEmailIgnoreCase(String email);

  Optional<User> findByEmailIgnoreCase(String email);
}
