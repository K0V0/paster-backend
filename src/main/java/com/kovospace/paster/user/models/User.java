package com.kovospace.paster.user.models;

import com.kovospace.paster.item.models.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.List;

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

  private Boolean gdpr;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private List<Item> items;

  //@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  //private List<WsSession> wsSessions;

  @Transient
  private String jwtToken;

  private String avatarFileName;

  //TODO performance balanced last user activity tracking //MAYBE


}
