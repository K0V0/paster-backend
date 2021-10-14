package com.kovospace.paster.base.filters;

import com.kovospace.paster.base.services.JwtService;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private JwtService jwtService;

  public JwtAuthFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader("Authorization");
    String token = Optional.ofNullable(header)
        .map(t -> t.replace("Bearer", ""))
        .orElse("");
    System.out.println(token);
    long userId = 0;
    try {
      userId = jwtService.parse(token);
      System.out.println(userId);
    } catch (JwtException e) {
      // do sth
    }

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
        userId, null, null
    ));
    filterChain.doFilter(request, response);

  }
}
