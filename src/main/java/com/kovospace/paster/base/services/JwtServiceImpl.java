package com.kovospace.paster.base.services;

import com.kovospace.paster.base.exceptions.jwtTokenException.InvalidJwtTokenException;
import com.kovospace.paster.user.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

  private TimeService timeService;
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
    this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
  }

  @Override
  public String generate(User user) {
    Claims claims = Jwts.claims();
    claims.put("userId", user.getId());
    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(timeService.getDate())
        .setExpiration(timeService.getDateAfter(validDays))
        .signWith(secretKey)
        .compact();
    return token;
  }

  @Override
  public long parse(String jwtToken) throws InvalidJwtTokenException {
    if (jwtToken != null) {
      if (jwtToken.startsWith(prefix)) {
        if (jwtToken.replace(prefix, "").trim().length() == 0) {
          throw new InvalidJwtTokenException("JWT Token is missing.");
        }
        Claims claims = jwtParser
            .parseClaimsJws(jwtToken.replace(prefix, ""))
            .getBody();
        return Long.parseLong(claims.get("userId").toString());

      }
      throw new InvalidJwtTokenException("Token prefix invalid or missing.");
    }
    throw new InvalidJwtTokenException("JWT Token is missing.");
  }
}
