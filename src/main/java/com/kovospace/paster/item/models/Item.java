package com.kovospace.paster.item.models;

import com.kovospace.paster.item.converters.PlatformEnumConverter;
import com.kovospace.paster.item.dtos.PlatformEnum;
import com.kovospace.paster.user.models.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Enumerated(EnumType.ORDINAL)
  @Convert(converter = PlatformEnumConverter.class)
  private PlatformEnum platform;

  @Column
  private String deviceName;
  @ManyToOne
  private User user;

  @Column(length = 4194304)
  private String text;

  @CreationTimestamp
  private Timestamp createdAt;

  @Transient
  private String preview;

  @Transient
  private boolean isLarge;

}
