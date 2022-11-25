package com.kovospace.paster.base.configurations.values;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "item")
@Getter
@Setter
public class ItemValues {
    private Set<String> extensionsWithPreview;
    private int textPreviewLength;
}
