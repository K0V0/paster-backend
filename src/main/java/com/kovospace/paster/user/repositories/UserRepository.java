package com.kovospace.paster.user.repositories;

import com.kovospace.paster.user.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findFirstByName(String name);
}
