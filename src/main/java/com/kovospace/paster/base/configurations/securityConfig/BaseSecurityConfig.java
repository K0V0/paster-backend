package com.kovospace.paster.base.configurations.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

public abstract class BaseSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // TODO ulozit allowed origins niekde do configu
        configuration.setAllowedOrigins(Arrays.asList(
                "http://0.0.0.0:4200",
                "http://0.0.0.0:6060",
                "http://kovo.space:4200",
                "http://kovo.space:6060",
                "http://localhost:4200",
                "http://localhost:6060",
                "http://paster.cloud",
                "http://www.paster.cloud",
                "https://paster.cloud",
                "https://www.paster.cloud"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token", "x-api-key"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
