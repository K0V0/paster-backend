package com.kovospace.paster.base.unit;

import com.kovospace.paster.base.services.JwtService;
import com.kovospace.paster.base.services.JwtServiceImpl;
import com.kovospace.paster.base.services.TimeService;
import com.kovospace.paster.user.models.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

@TestMethodOrder(OrderAnnotation.class)
public class JwtServiceTest {

  private String privateKey = "bxjMP5G_LzXaBFI4eH1Y4TNStAIX6DLT0KNdt4DKm74";
  private String prefix = "bearer";
  private TimeService timeService;
  private JwtService jwtService;
  private SecretKey secretKey;
  private JwtParser jwtParser;
  private User user;
  private String jwtToken;

  @BeforeEach
  public void setUp() {
    this.user = new User();
    user.setId(1);
    this.secretKey = Keys.hmacShaKeyFor(privateKey.getBytes(StandardCharsets.UTF_8));
    this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();

    timeService = Mockito.mock(TimeService.class);
    jwtService = new JwtServiceImpl(timeService);

    ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
    ReflectionTestUtils.setField(jwtService, "jwtParser", jwtParser);
    ReflectionTestUtils.setField(jwtService, "prefix", prefix);

    jwtToken = prefix + " " + jwtService.generate(user);
  }

  @Test
  @Order(1)
  public void generation() {User user = new User();
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjB9.sB_inUhsFYR9TEvpZAwp0jcPO8P0Rpn7iZhlcfIOwi4";
    Assert.assertEquals(token, jwtService.generate(user));
  }

  @Test
  @Order(2)
  public void hasValidData() {
    long userIdFromToken = jwtService.parse(jwtToken);
    /*Claims claims = jwtParser
        .parseClaimsJws(jwtToken.replace("bearer ", ""))
        .getBody();*/
    //long startTime = Long.parseLong(claims.get("iat").toString());
    //long endTime = Long.parseLong(claims.get("exp").toString());
    //long userIdFromToken = Long.parseLong(claims.get("userId").toString());
    //System.out.println(startTime);
    //System.out.println(endTime);
    //System.out.println("---------------------------");
    Assertions.assertEquals(userIdFromToken, 1);
    //Assertions.assertEquals(60 * 60 * 24 * 365, endTime - startTime);
  }

  @Test
  @Order(3)
  public void tokenSignatureIsMalformed() {
    Assertions.assertThrows(
        SignatureException.class,
        () -> jwtService.parse(jwtToken.substring(0, jwtToken.length() - 1))
    );
  }

  @Test
  @Order(4)
  public void tokenBearerPrefixIsMissing() {
    Assertions.assertThrows(
        JwtException.class,
        () -> jwtService.parse(jwtToken.replace("bearer ", ""))
    );
  }

  @Test
  @Order(5)
  public void tokenHeaderIsMalformed() {
    Assertions.assertThrows(
        MalformedJwtException.class,
        () -> jwtService.parse(prefix + " " + jwtToken.substring(8))
    );
  }

  @Test
  @Order(6)
  public void tokenBodyIsMalformed() {
    int beginIndex = jwtToken.indexOf(".");
    String malformedToken = jwtToken.substring(0, beginIndex+1) + jwtToken.substring(beginIndex+2);

    Assertions.assertThrows(
        SignatureException.class,
        () -> jwtService.parse(malformedToken)
    );
  }

  @Test
  @Order(7)
  public void tokenIsMissing() {
    Assertions.assertThrows(
        JwtException.class,
        () -> jwtService.parse("")
    );
  }

  @Test
  @Order(8)
  public void tokenIsMissing2() {
    Assertions.assertThrows(
        JwtException.class,
        () -> jwtService.parse("bearer")
    );
  }

  @Test
  @Order(9)
  public void tokenIsMissing3() {
    Assertions.assertThrows(
        JwtException.class,
        () -> jwtService.parse("bearer ")
    );
  }

  // TODO expired token test
  /*
  @Test
  @Order(10)
  public void tokenIsExpired() throws InvalidJwtTokenException {
    Mockito
        .when(timeService.getDateAfter(365))
        .thenReturn(Date.from(Instant.ofEpochSecond(1234567890)));
    Mockito
        .when(timeService.getDate())
        .thenReturn(Date.from(Instant.ofEpochSecond(1234567891)));
    jwtService = new JwtServiceImpl(timeService);
    ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
    ReflectionTestUtils.setField(jwtService, "jwtParser", jwtParser);
    ReflectionTestUtils.setField(jwtService, "prefix", prefix);

    jwtToken = prefix + " " + jwtService.generate(user);

    Assertions.assertThrows(
        InvalidJwtTokenException.class,
        () -> jwtService.parse(jwtToken)
    );
  }
  */
}
