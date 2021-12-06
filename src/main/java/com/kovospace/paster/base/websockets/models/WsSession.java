package com.kovospace.paster.base.websockets.models;

import com.kovospace.paster.user.models.User;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WsSession {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String sessionId;

  @ManyToOne
  private User user;

  public WsSession(User user, String sessionId) {
    this.sessionId = sessionId;
    this.user = user;
  }

}
