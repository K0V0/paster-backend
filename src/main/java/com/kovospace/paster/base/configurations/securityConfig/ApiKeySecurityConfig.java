package com.kovospace.paster.base.configurations.securityConfig;

import com.kovospace.paster.base.exceptions.FiltersExceptionHandler;
import com.kovospace.paster.base.filters.ApiKeyAuthFilter;
import com.kovospace.paster.base.services.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@Order(1)
public class ApiKeySecurityConfig extends WebSecurityConfigurerAdapter {

    private ApiKeyService apiKeyService;
    private FiltersExceptionHandler exceptionsFilter;

    private CorsConfigurationSource corsConfigurationSource;

    @Autowired
    public ApiKeySecurityConfig(
            ApiKeyService apiKeyService,
            FiltersExceptionHandler exceptionsFilter,
            CorsConfigurationSource corsConfigurationSource
    ) {
        this.apiKeyService = apiKeyService;
        this.exceptionsFilter = exceptionsFilter;
        this.corsConfigurationSource = corsConfigurationSource;
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
                .antMatchers(HttpMethod.GET, "/websocket").permitAll()
                .antMatchers(HttpMethod.GET, "/websocket/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(
                        exceptionsFilter,
                        CorsFilter.class
                )
                .addFilterAfter(
                        filter,
                        CorsFilter.class
                );
    }

}
