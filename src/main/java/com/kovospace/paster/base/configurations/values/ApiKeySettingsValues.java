package com.kovospace.paster.base.configurations.values;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class ApiKeySettingsValues {
    private String apiKeyToken;
    private String apiKeyHeader;
}