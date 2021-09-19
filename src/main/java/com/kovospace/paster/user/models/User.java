package com.kovospace.paster.user.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;
  private String name;
  private String pasword;
  @Transient private String jwtToken;
}
