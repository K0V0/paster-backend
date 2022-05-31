package com.kovospace.paster.base.configurations;

import com.kovospace.paster.base.filters.ApiKeyAuthFilter;
import com.kovospace.paster.base.filters.ExceptionsHandlerFilter;
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

        //private final ExceptionsHandlerFilter exceptionsFilter;
        private final SimpleCorsFilter simpleCorsFilter;

        @Autowired
        public BasicAndExceptions(/*ExceptionsHandlerFilter exceptionsFilter,*/ SimpleCorsFilter simpleCorsFilter) {
            //this.exceptionsFilter = exceptionsFilter;
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
                    .and()
                    .addFilterBefore(
                            simpleCorsFilter,
                            CorsFilter.class);
//                    .addFilterBefore(
//                        exceptionsFilter,
//                        SimpleCorsFilter.class);
        }
    }

    @Configuration
    @Order(2)
    public static class ApiKeyWebSecurity extends WebSecurityConfigurerAdapter {

        private final ApiKeyAuthFilter apiKeyAuthFilter;
        //private final ExceptionsHandlerFilter exceptionsFilter;

        @Autowired
        public ApiKeyWebSecurity(ApiKeyAuthFilter apiKeyAuthFilter/*, ExceptionsHandlerFilter exceptionsFilter*/) {
            this.apiKeyAuthFilter = apiKeyAuthFilter;
            //this.exceptionsFilter = exceptionsFilter;
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
//                    .addFilterBefore(
//                            exceptionsFilter,
//                            ApiKeyAuthFilter.class);
        }
    }

    @Configuration
    @Order(3)
    public static class JwtTokenWebSecurity extends WebSecurityConfigurerAdapter {

        private final JwtAuthFilter jwtAuthFilter;
        //private final ExceptionsHandlerFilter exceptionsFilter;

        @Autowired
        public JwtTokenWebSecurity(JwtAuthFilter jwtAuthFilter/*, ExceptionsHandlerFilter exceptionsFilter*/) {
            this.jwtAuthFilter = jwtAuthFilter;
            //this.exceptionsFilter = exceptionsFilter;
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
//                    .addFilterBefore(
//                            exceptionsFilter,
//                            JwtAuthFilter.class);
        }
    }

}
