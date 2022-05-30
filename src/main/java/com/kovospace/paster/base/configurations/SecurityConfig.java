package com.kovospace.paster.base.configurations;

import com.kovospace.paster.base.exceptions.FiltersExceptionHandler;
import com.kovospace.paster.base.filters.ApiKeyAuthFilter;
import com.kovospace.paster.base.filters.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApiKeyAuthFilter apiKeyAuthFilter;
    private final JwtAuthFilter jwtAuthFilter;
    private final FiltersExceptionHandler exceptionsFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Autowired
    public SecurityConfig(
            ApiKeyAuthFilter apiKeyAuthFilter,
            JwtAuthFilter jwtAuthFilter,
            FiltersExceptionHandler exceptionsFilter,
            CorsConfigurationSource corsConfigurationSource
    ) {
        this.apiKeyAuthFilter = apiKeyAuthFilter;
        this.jwtAuthFilter = jwtAuthFilter;
        this.exceptionsFilter = exceptionsFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .cors()
                .and()
                .exceptionHandling()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("*").permitAll()
                .and()
                .addFilterAfter(
                        exceptionsFilter,
                        CorsFilter.class)
                .addFilterAfter(
                        apiKeyAuthFilter,
                        CorsFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v*/user/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v*/user/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // TODO ulozit allowed origins niekde do configu
        configuration.setAllowedOrigins(Arrays.asList(
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
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        //configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
