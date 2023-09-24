package com.kovospace.paster.base.configurations;

import com.kovospace.paster.base.filters.ApiKeyAuthFilter;
import com.kovospace.paster.base.filters.JwtAuthFilter;
import com.kovospace.paster.base.filters.SimpleCorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@EnableWebSecurity
public class SecurityConfig {

    @Value("#{'${app.swagger-ui.paths}'.split(',')}")
    private List<String> swaggerUiPaths;

    @Configuration
    @Order(1)
    public class BasicAndExceptions extends WebSecurityConfigurerAdapter {

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
                    .antMatchers(swaggerUiPaths.toArray(new String[0])).permitAll()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers(HttpMethod.GET, "/websocket").permitAll() //TODO do konštánt
                    .and()
                    .addFilterBefore(
                            simpleCorsFilter,
                            CorsFilter.class);
        }
    }

    @Configuration
    @Order(2)
    public class ApiKeyWebSecurity extends WebSecurityConfigurerAdapter {

        private final ApiKeyAuthFilter apiKeyAuthFilter;

        @Autowired
        public ApiKeyWebSecurity(ApiKeyAuthFilter apiKeyAuthFilter) {
            this.apiKeyAuthFilter = apiKeyAuthFilter;
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .authorizeRequests()
                    .antMatchers(swaggerUiPaths.toArray(new String[0])).permitAll()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers(HttpMethod.GET, "/websocket").permitAll() //TODO konštanty
                    .and()
                    .addFilterBefore(
                            apiKeyAuthFilter,
                            UsernamePasswordAuthenticationFilter.class);
        }
    }

    @Configuration
    @Order(3)
    public class JwtTokenWebSecurity extends WebSecurityConfigurerAdapter {

        private final JwtAuthFilter jwtAuthFilter;

        @Autowired
        public JwtTokenWebSecurity(JwtAuthFilter jwtAuthFilter) {
            this.jwtAuthFilter = jwtAuthFilter;
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .authorizeRequests()
                    .antMatchers(swaggerUiPaths.toArray(new String[0])).permitAll()
                    .antMatchers(HttpMethod.POST, "/api/v*/user/**").permitAll() //TODO do konštánt
                    .antMatchers(HttpMethod.GET, "/api/v*/user/**").permitAll() //TODO do konštánt
                    .antMatchers(HttpMethod.GET, "/websocket").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilterBefore(
                            jwtAuthFilter,
                            UsernamePasswordAuthenticationFilter.class);
        }
    }

}
