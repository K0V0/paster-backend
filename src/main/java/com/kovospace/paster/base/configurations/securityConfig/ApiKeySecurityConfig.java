package com.kovospace.paster.base.configurations.securityConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.base.exceptions.FiltersExceptionHandler;
import com.kovospace.paster.base.filters.ApiKeyAuthFilter;
import com.kovospace.paster.base.services.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@Order(2)
public class ApiKeySecurityConfig extends BaseSecurityConfig {

    private ApiKeyService apiKeyService;
    private FiltersExceptionHandler exceptionsFilter;
    private ObjectMapper objectMapper;

    @Autowired
    public ApiKeySecurityConfig(
            ApiKeyService apiKeyService,
            ObjectMapper objectMapper,
            FiltersExceptionHandler exceptionsFilter)
    {
        this.apiKeyService = apiKeyService;
        this.objectMapper = objectMapper;
        this.exceptionsFilter = exceptionsFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        ApiKeyAuthFilter filter = new ApiKeyAuthFilter(apiKeyService);

        filter.setAuthenticationManager(authentication -> {
            // filter aj tak vyhadzuje exceptions ak sa daco nepodari
            authentication.setAuthenticated(true);
            return authentication;
        });

        httpSecurity
                .csrf().disable()
                .cors()
                .and()
                .exceptionHandling()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/websocket").permitAll()
                .antMatchers("/stomp").permitAll()
                .and()
                .addFilterBefore(
                        exceptionsFilter,
                        CorsFilter.class
                )
                .addFilter(filter).authorizeRequests().anyRequest().authenticated();
    }

}
