package com.kovospace.paster.base.configurations.securityConfig;

import com.kovospace.paster.base.exceptions.FiltersExceptionHandler;
import com.kovospace.paster.base.filters.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@EnableWebSecurity
@Configuration
@Order(2)
public class JwtTokenSecurityConfig extends WebSecurityConfigurerAdapter {

  private JwtAuthFilter jwtAuthFilter;
  private FiltersExceptionHandler filtersExceptionHandler;
  private CorsConfigurationSource corsConfigurationSource;

  @Autowired
  public JwtTokenSecurityConfig(
          JwtAuthFilter jwtAuthFilter,
          FiltersExceptionHandler filtersExceptionHandler,
          CorsConfigurationSource corsConfigurationSource
  ) {
      this.jwtAuthFilter = jwtAuthFilter;
      this.filtersExceptionHandler = filtersExceptionHandler;
      this.corsConfigurationSource = corsConfigurationSource;
  }

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
        .antMatchers(HttpMethod.POST, "/api/v*/user/**").permitAll()
        .antMatchers(HttpMethod.GET, "/api/v*/user/**").permitAll()
        //.antMatchers(HttpMethod.GET, "/websocket").permitAll()
        //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(
                filtersExceptionHandler,
                UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class);
  }
}
