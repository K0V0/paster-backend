package com.kovospace.paster.base.websockets.repositories;

import com.kovospace.paster.base.websockets.models.UserSession;
import com.kovospace.paster.user.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

  Optional<UserSession> findByUserAndAndSessionId(User user, String sessionId);

  List<UserSession> findAllBySessionIdEquals(String sessionId);

  List<UserSession> findAllByUserEquals(User user);

}
