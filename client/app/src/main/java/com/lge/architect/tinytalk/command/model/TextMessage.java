package com.lge.architect.tinytalk.command.model;

import org.joda.time.DateTime;
import org.joda.time.Instant;

import java.util.HashSet;
import java.util.Set;

public class TextMessage {
  public static final String URI = "txtmsg";

  private String sender;
  private Set<String> receivers;
  private String message;
  private long timestamp;

  public TextMessage(String sender, Set<String> receivers, String message) {
    this.sender = sender;
    if (receivers.contains(sender)) {
      receivers.remove(sender);
    }
    this.receivers = receivers;
    this.message = message;
    this.timestamp = Instant.now().getMillis();
  }

  public Set<String> getParticipants() {
    Set<String> participants = new HashSet<>(receivers);
    participants.add(sender);

    return participants;
  }

  public DateTime getDateTime() {
    return new DateTime(timestamp);
  }

  public String getBody() {
    return message;
  }

  public String getSender() {
    return sender;
  }
}
