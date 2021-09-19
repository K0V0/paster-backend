package com.kovospace.paster.user.repositories;

import com.kovospace.paster.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
