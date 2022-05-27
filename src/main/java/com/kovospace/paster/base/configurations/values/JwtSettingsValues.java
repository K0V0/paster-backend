package com.kovospace.paster.base.configurations.values;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtSettingsValues {
  private String secretKey;
  private String prefix;
  private String headerType;
  private int validDays;
}
