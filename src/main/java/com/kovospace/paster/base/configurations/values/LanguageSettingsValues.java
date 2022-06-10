package com.kovospace.paster.base.configurations.values;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class LanguageSettingsValues {
    private String defaultLanguageCode;
    private String languageHeader;
}
