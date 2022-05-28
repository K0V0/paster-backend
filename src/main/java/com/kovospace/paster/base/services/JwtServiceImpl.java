package com.kovospace.paster.base.services;

import com.kovospace.paster.user.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JwtServiceImpl implements JwtService {

  private final TimeService timeService;
  private SecretKey secretKey;
  private JwtParser jwtParser;

  @Value("${jwt.secret-key}")
  private String privateKey;

  @Value("${jwt.valid-days}")
  private int validDays;

  @Value("${jwt.prefix}")
  private String prefix;

  @Autowired
  public JwtServiceImpl(TimeService timeService) {
    this.timeService = timeService;
  }

  @PostConstruct
  public void init() {
    this.secretKey = Keys.hmacShaKeyFor(privateKey.getBytes(StandardCharsets.UTF_8));
    this.jwtParser = Jwts.parserBuilder()
        .setSigningKey(privateKey.getBytes(StandardCharsets.UTF_8))
        .build();
  }

  @Override
  public String generate(User user) {
    Claims claims = Jwts.claims();
    claims.put("userId", user.getId());
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(timeService.getDate())
        .setExpiration(timeService.getDateAfter(validDays))
        .signWith(secretKey)
        .compact();
  }

  @Override
  public long parse(String jwtToken) throws JwtException {
    if (jwtToken != null) {
      if (prefix != null && !prefix.equals("")) {
        if (jwtToken.startsWith(prefix)) {
          if (jwtToken.replace(prefix, "").trim().length() == 0) {
            throw new JwtException("general.endpoint.authentication.jwt.missing");
          }
          Claims claims = jwtParser
                  .parseClaimsJws(jwtToken.replace(prefix, "").trim())
                  .getBody();
          return Long.parseLong(claims.get("userId").toString());

        }
        throw new JwtException("general.endpoint.authentication.jwt.prefix.wrong");
      }
      throw new JwtException("general.endpoint.authentication.jwt.prefix.missing");
    }
    throw new JwtException("general.endpoint.authentication.jwt.missing");
  }
  @Override
  public String getPrefix() {
    return this.prefix;
  }

}
