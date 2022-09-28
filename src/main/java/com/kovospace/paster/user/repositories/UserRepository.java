package com.kovospace.paster.user.repositories;

import com.kovospace.paster.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findFirstByName(String name);

  User getFirstByName(String name);

  Optional<User> findFirstByEmail(String email);

  User getFirstByEmail(String email);
}
