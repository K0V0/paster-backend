package com.kovospace.paster.base.websockets.repositories;

import com.kovospace.paster.base.websockets.models.WsSession;
import com.kovospace.paster.user.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WsSessionRepository extends JpaRepository<WsSession, Long> {

  Optional<WsSession> findByUserAndAndSessionId(User user, String sessionId);

  List<WsSession> findAllBySessionIdEquals(String sessionId);

  List<WsSession> findAllByUserEquals(User user);

}
