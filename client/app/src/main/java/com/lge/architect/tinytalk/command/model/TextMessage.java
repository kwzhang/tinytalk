package com.lge.architect.tinytalk.command.model;

import org.joda.time.DateTime;
import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.List;

public class TextMessage {
  public static final String URI = "txtmsg";

  private String sender;
  private List<String> receivers;
  private String message;
  private long timestamp;

  public TextMessage(String sender, List<String> receivers, String message) {
    this.sender = sender;
    this.receivers = receivers;
    this.message = message;
    this.timestamp = Instant.now().getMillis();
  }

  public List<String> getParticipants() {
    List<String> participants = new ArrayList<>(receivers);
    participants.add(sender);

    return participants;
  }

  public DateTime getDateTime() {
    return new DateTime(timestamp);
  }

  public String getBody() {
    return this.message;
  }
}
