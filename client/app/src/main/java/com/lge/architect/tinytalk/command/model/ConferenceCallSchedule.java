package com.lge.architect.tinytalk.command.model;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.List;

public class ConferenceCallSchedule {
  public static final String URI = "ccrequest";

  private List<String> members;
  private DateTime startDatetime;
  private DateTime endDatetime;

  public ConferenceCallSchedule(List<String> members, DateTime startDatetime, DateTime endDatetime) {
    this.members = members;
    this.startDatetime = startDatetime;
    this.endDatetime = endDatetime;
  }
}
