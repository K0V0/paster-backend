package com.kovospace.paster.base.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.base.services.StringsService;
import io.jsonwebtoken.JwtException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@Order(3)
public class JwtAuthFilter extends BaseFilter {

  private final JwtService jwtService;

  public JwtAuthFilter(JwtService jwtService, ObjectMapper objectMapper, StringsService stringsService) {
    super(objectMapper, stringsService);
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
  throws ServletException, IOException
  {
    long userId = -1;
    try {
      userId = Optional.ofNullable(request.getHeader("Authorization"))
              .filter(head -> head.length() > 0)
              .map(t -> jwtService.parse(t))
              .orElse(0L);
    } catch (JwtException e) {
      handleError(response, e.getMessage(), HttpStatus.FORBIDDEN);
      return;
    }

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
        userId, null, null
    ));
    filterChain.doFilter(request, response);
  }
}
