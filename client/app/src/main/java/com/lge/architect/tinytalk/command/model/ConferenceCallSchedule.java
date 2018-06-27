package com.lge.architect.tinytalk.command.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Set;

public class ConferenceCallSchedule {
  public static final String URI = "ccrequest";

  private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATETIME_FORMAT);

  private Set<String> members;
  private String startDatetime;
  private String endDatetime;

  public ConferenceCallSchedule(Set<String> members, DateTime startDatetime, DateTime endDatetime) {
    this.members = members;
    this.startDatetime = dateTimeFormatter.print(startDatetime);
    this.endDatetime = dateTimeFormatter.print(endDatetime);
  }
}
