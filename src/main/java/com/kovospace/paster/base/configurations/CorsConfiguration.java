package com.kovospace.paster.base.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Arrays;

@Configuration
public class CorsConfiguration extends WebMvcConfigurationSupport {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://0.0.0.0:4200",
                        "https://0.0.0.0:4200",
                        "http://0.0.0.0:5999",
                        "https://0.0.0.0:5999",
                        "http://192.168.100.247:5999",
                        "https://192.168.100.247:5999",
                        "http://0.0.0.0:6060",
                        "https://0.0.0.0:6060",
                        "http://kovo.space:4200",
                        "https://kovo.space:4200",
                        "http://kovo.space:6060",
                        "https://kovo.space:6060",
                        "http://localhost:4200",
                        "https://localhost:4200",
                        "http://localhost:6060",
                        "https://localhost:6060",
                        "http://paster.cloud",
                        "http://www.paster.cloud",
                        "https://paster.cloud",
                        "https://www.paster.cloud"
                )
                .allowedMethods("*")
                .allowCredentials(true);
    }
}