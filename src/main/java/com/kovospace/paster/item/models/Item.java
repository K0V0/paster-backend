package com.kovospace.paster.item.models;

import com.kovospace.paster.item.dtos.PlatformEnum;
import com.kovospace.paster.item.mappings.attributeConverters.PlatformEnumConverter;
import com.kovospace.paster.user.models.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.sql.Timestamp;

/** Contract
 * String data not empty, File file empty       => TEXT data
 * String data empty, File file not empty       => not yet uploaded/corrupted file
 * String data not empty, File file not empty   => uploaded file
 */

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

  private String deviceName;
  @ManyToOne
  private User user;

  @Column(length = 4194304)
  private String data;

  @CreationTimestamp
  private Timestamp createdAt;

  /** API v2 functionality */

  @OneToOne(cascade = CascadeType.ALL)
  private File file;

}
