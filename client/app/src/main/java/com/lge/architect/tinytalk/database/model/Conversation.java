package com.lge.architect.tinytalk.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Conversation.TABLE_NAME)
public class Conversation extends DatabaseModel {
  public static final String TABLE_NAME = "conversations";
  public static final String KEY = "conversation_key";

  public static final String USER = "user";
  public static final String DATETIME = "datetime";

  @DatabaseField(columnName = USER)
  protected String users;

  @DatabaseField(columnName = DATETIME)
  protected long time;

  protected Conversation() {
  }

  public Conversation(String users, long timeMillis) {
    this.users = users;
    this.time = timeMillis;
  }
}
