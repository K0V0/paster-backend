package com.kovospace.paster.base.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .cors()
        .and()
        .exceptionHandling()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/user/**").permitAll()
        .antMatchers(HttpMethod.GET, "/user/**").permitAll()
        .anyRequest().authenticated();
        /*.and()
        addFilterBefore(
            jwtExceptionCatcherFilter,
            UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(
            jwtAuthorizationFilter,
            UsernamePasswordAuthenticationFilter.class);*/
  }

}
