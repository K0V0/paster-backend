package com.kovospace.paster.base.configurations;

import com.kovospace.paster.base.exceptions.FiltersExceptionHandler;
import com.kovospace.paster.base.filters.ApiKeyAuthFilter;
import com.kovospace.paster.base.filters.JwtAuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Configuration
    @Order(1)
    public static class BasicAndExceptions extends WebSecurityConfigurerAdapter {

        private final FiltersExceptionHandler exceptionsFilter;

        @Autowired
        public BasicAndExceptions(FiltersExceptionHandler exceptionsFilter) {
            this.exceptionsFilter = exceptionsFilter;
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
                    .and()
                    .addFilterBefore(
                        exceptionsFilter,
                        UsernamePasswordAuthenticationFilter.class);
        }
    }

//    @Configuration
//    @Order(2)
//    public static class ApiKeyWebSecurity extends WebSecurityConfigurerAdapter {
//
//        private final ApiKeyAuthFilter apiKeyAuthFilter;
//
//        @Autowired
//        public ApiKeyWebSecurity(ApiKeyAuthFilter apiKeyAuthFilter) {
//            this.apiKeyAuthFilter = apiKeyAuthFilter;
//        }
//
//        @Override
//        protected void configure(HttpSecurity httpSecurity) throws Exception {
//            httpSecurity
//                    .authorizeRequests()
//                    .antMatchers(HttpMethod.OPTIONS).permitAll()
//                    .antMatchers(HttpMethod.GET, "/websocket").permitAll()
//                    .and()
//                    /*.addFilterBefore(
//                            exceptionsFilter,
//                            UsernamePasswordAuthenticationFilter.class)*/
//                    .addFilterBefore(
//                            apiKeyAuthFilter,
//                            UsernamePasswordAuthenticationFilter.class);
//        }
//    }

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
