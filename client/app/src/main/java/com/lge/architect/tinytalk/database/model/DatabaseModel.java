package com.lge.architect.tinytalk.database.model;

import com.j256.ormlite.field.DatabaseField;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class DatabaseModel {
  public static final String _ID = "_id";

  @DatabaseField(generatedId = true, columnName = _ID)
  protected transient long id;

  protected static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATETIME_FORMAT);

  public long getId() {
    return id;
  }
}
