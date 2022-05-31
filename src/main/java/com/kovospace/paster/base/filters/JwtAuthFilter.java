package com.kovospace.paster.base.filters;

import com.kovospace.paster.base.services.JwtService;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@Order(3)
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  public JwtAuthFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
  throws ServletException, IOException
  {
    System.out.println("JWT token filter called");

    String header = request.getHeader("Authorization");
    String token = Optional.ofNullable(header)
        .map(t -> t.replace("Bearer", ""))
        .orElse("");
    long userId = 0;
    try {
      userId = jwtService.parse(token);
    } catch (JwtException e) {
      //todo do sth or throw ?
    }

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
        userId, null, null
    ));
    filterChain.doFilter(request, response);
  }
}
