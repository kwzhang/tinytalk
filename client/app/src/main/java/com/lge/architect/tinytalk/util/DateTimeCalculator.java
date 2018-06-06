package com.lge.architect.tinytalk.util;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

public class DateTimeCalculator {
  public static String getString(DateTime dateTime) {
    DateTime now = DateTime.now();
    Interval today = new Interval(now.withTimeAtStartOfDay(), now.plusDays(1).withTimeAtStartOfDay());
    Interval yesterday = new Interval(now.minus(1).withTimeAtStartOfDay(), now.withTimeAtStartOfDay());
    Interval hoursAgo = new Interval(now.minusDays(1), now.minusHours(1));
    Interval minutesAgo = new Interval(now.minusHours(1), now.minusMinutes(1));
    Interval secondsAgo = new Interval(now.minusMinutes(1), now);

    if (hoursAgo.contains(dateTime)) {
      int hours = Hours.hoursBetween(dateTime, now).getHours();

      if (hours == 1) { // FIXME: Use plural strings
        return hours + " hour ago";
      }
      return hours + " hours ago";
    } else if (minutesAgo.contains(dateTime)) {
      return Minutes.minutesBetween(dateTime, now).getMinutes() + " minutes ago";
    } else if (secondsAgo.contains(dateTime)) {
      return Seconds.secondsBetween(dateTime, now) + " seconds ago";
    } else if (yesterday.contains(dateTime)) {
      return "yesterday";
    } else {
      return " days ago";
    }
  }
}
