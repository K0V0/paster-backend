package com.kovospace.paster.base.services;

import java.time.Instant;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {

  @Override
  public long getTime() {
    return Instant.now().getEpochSecond();
  }

  @Override
  public long getTimeAfter(int sec) {
    return Instant.now().getEpochSecond() + sec;
  }

  @Override
  public int getTimeBetween(long from, long to) {
    if (from <= to) {
      var res = to - from;
      if (res <= Integer.MAX_VALUE) {
        return (int) res;
      } else {
        throw new ArithmeticException("Result too long");
      }
    } else {
      throw new IllegalArgumentException("Value of 'from' must be less than 'to'");
    }
  }

  @Override
  public Date getDate() {
    return Date.from(Instant.now());
  }

  public Date getDateAfter(int days) {
    return new Date(Instant.now().getEpochSecond() * 1000 + 1000L * 60 * 60 * 24 * days);
  }
}
