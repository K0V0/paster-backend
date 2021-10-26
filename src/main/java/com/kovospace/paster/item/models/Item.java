package com.kovospace.paster.item.models;

import com.kovospace.paster.user.models.User;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private User user;

  @Column(length = 4194304)
  private String text;

  @CreationTimestamp
  private Date createdAt;

  @Transient
  private String preview;

  @Transient
  private boolean isLarge;

}
