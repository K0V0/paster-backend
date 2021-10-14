package com.kovospace.paster.base.unit;

import com.kovospace.paster.base.services.TimeService;
import com.kovospace.paster.base.services.TimeServiceImpl;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class TimeServiceTest {

  private TimeService timeService = new TimeServiceImpl();

  private void waitIfNeeded() throws InterruptedException {
    var currentMillis = Instant.now().get(ChronoField.MILLI_OF_SECOND);
    if (currentMillis > 500) {
      Thread.sleep(1000 - currentMillis);
    }
  }

  @Test
  @Order(1)
  public void getTimeOk() throws InterruptedException {
    waitIfNeeded();
    Assertions.assertEquals(Instant.now().getEpochSecond(), timeService.getTime());
  }

  @Test
  @Order(2)
  public void getTimeAfterOk() throws InterruptedException {
    waitIfNeeded();
    var diff = 5457;
    Assertions.assertEquals(Instant.now().getEpochSecond() + diff, timeService.getTimeAfter(diff));
  }

  @Test
  @Order(3)
  public void getTimeBetweenOk() {
    var from = 1622742573L;
    var diff = 5457;
    var to = from + diff;
    Assertions.assertEquals(diff, timeService.getTimeBetween(from, to));
  }

  @Test
  @Order(4)
  public void getTimeBetweenWrongOrder() {
    var from = 1622742573L;
    var diff = 5457;
    var to = from - diff;
    var exception = Assertions
        .assertThrows(IllegalArgumentException.class, () -> timeService.getTimeBetween(from, to));
    Assertions.assertEquals("Value of 'from' must be less than 'to'", exception.getMessage());
  }

  @Test
  @Order(5)
  public void getTimeBetweenDifferenceTooBig() {
    var from = 1622742573L;
    var diff = 1L + Integer.MAX_VALUE;
    var to = from + diff;
    var exception = Assertions
        .assertThrows(ArithmeticException.class, () -> timeService.getTimeBetween(from, to));
    Assertions.assertEquals("Result too long", exception.getMessage());
  }

  @Test
  @Order(6)
  public void getDateCorrect() {
    Assertions.assertEquals(Date.from(Instant.now()), timeService.getDate());
  }

  @Test
  @Order(7)
  public void getDateAfterCorrect() throws InterruptedException {
    waitIfNeeded();
    long today = timeService.getDate().toInstant().getEpochSecond();
    long tommorow = timeService.getDateAfter(1).toInstant().getEpochSecond();
    Assertions.assertEquals(86400, tommorow - today);
  }

  @Test
  @Order(8)
  public void zeroCannotBreakGetDaysAfter() throws InterruptedException {
    waitIfNeeded();
    Assertions.assertEquals(timeService.getDate().toInstant().getEpochSecond(),
        timeService.getDateAfter(0).toInstant().getEpochSecond());
  }

  @Test
  @Order(8)
  public void negativeCannotBreakGetDaysAfter() throws InterruptedException {
    waitIfNeeded();
    Assertions.assertEquals(timeService.getDate().toInstant().getEpochSecond() - 86400,
        timeService.getDateAfter(-1).toInstant().getEpochSecond());
  }

}