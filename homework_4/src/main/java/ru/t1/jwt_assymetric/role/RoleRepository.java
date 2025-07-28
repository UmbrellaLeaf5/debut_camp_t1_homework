package ru.t1.jwt_assymetric.role;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
  Optional<Role> findByName(String name);
}
