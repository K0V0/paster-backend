package com.kovospace.paster.user.models;

import com.kovospace.paster.base.websockets.models.UserSession;
import com.kovospace.paster.item.models.Item;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(unique=true)
  private String name;

  private String pasword;

  private String email;

  @OneToMany(mappedBy = "user")
  private List<Item> items;

  @OneToMany(mappedBy = "user")
  private List<UserSession> userSessions;

  @Transient
  private String jwtToken;

}
