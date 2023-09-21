package com.kovospace.paster.base.configurations;

import com.kovospace.paster.base.filters.ApiKeyAuthFilter;
import com.kovospace.paster.base.filters.JwtAuthFilter;
import com.kovospace.paster.base.filters.SimpleCorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @Order(1)
    public static class BasicAndExceptions extends WebSecurityConfigurerAdapter {

        private final SimpleCorsFilter simpleCorsFilter;

        @Autowired
        public BasicAndExceptions(SimpleCorsFilter simpleCorsFilter) {
            this.simpleCorsFilter = simpleCorsFilter;
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
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers(HttpMethod.GET, "/websocket").permitAll()
                    .antMatchers("/swagger-ui.html", "/v3/api-docs", "/swagger-ui/**", "/swagger-resources/**", "/public-api").permitAll()
                    .and()
                    .addFilterBefore(
                            simpleCorsFilter,
                            CorsFilter.class);
        }
    }

    @Configuration
    @Order(2)
    public static class ApiKeyWebSecurity extends WebSecurityConfigurerAdapter {

        private final ApiKeyAuthFilter apiKeyAuthFilter;

        @Autowired
        public ApiKeyWebSecurity(ApiKeyAuthFilter apiKeyAuthFilter) {
            this.apiKeyAuthFilter = apiKeyAuthFilter;
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers(HttpMethod.GET, "/websocket").permitAll()
                    .and()
                    .addFilterBefore(
                            apiKeyAuthFilter,
                            UsernamePasswordAuthenticationFilter.class);
        }
    }

    @Configuration
    @Order(3)
    public static class JwtTokenWebSecurity extends WebSecurityConfigurerAdapter {

        private final JwtAuthFilter jwtAuthFilter;

        @Autowired
        public JwtTokenWebSecurity(JwtAuthFilter jwtAuthFilter) {
            this.jwtAuthFilter = jwtAuthFilter;
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/api/v*/user/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v*/user/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/websocket").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilterBefore(
                            jwtAuthFilter,
                            UsernamePasswordAuthenticationFilter.class);
        }
    }

}
