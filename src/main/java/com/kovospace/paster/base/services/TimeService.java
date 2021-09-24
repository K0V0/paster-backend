package com.kovospace.paster.base.services;

import java.util.Date;

public interface TimeService {

  long getTime();

  long getTimeAfter(int sec);

  int getTimeBetween(long from, long to);

  Date getDate();

  Date getDateAfter(int days);

}
