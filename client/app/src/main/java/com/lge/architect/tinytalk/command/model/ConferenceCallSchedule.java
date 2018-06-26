package com.lge.architect.tinytalk.command.model;

import java.time.LocalDateTime;
import java.util.List;

public class ConferenceCallSchedule {
  public static final String URI = "ccrequest";

  private List<String> members;
  private LocalDateTime startDatetime;
  private LocalDateTime endDatetime;

  public ConferenceCallSchedule(List<String> members, LocalDateTime startDatetime, LocalDateTime endDatetime) {
    this.members = members;
    this.startDatetime = startDatetime;
    this.endDatetime = endDatetime;
  }
}
