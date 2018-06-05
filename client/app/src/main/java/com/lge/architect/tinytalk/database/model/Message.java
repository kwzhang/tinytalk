package com.lge.architect.tinytalk.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Message.TABLE_NAME)
public class Message extends DatabaseModel {
  public static final String TABLE_NAME = "messages";
  public static final String KEY = "message_key";

  public static final String CONTACT_ID = "contact_id";
  public static final String BODY = "body";
  public static final String DATETIME = "datetime";

  @DatabaseField(columnName = CONTACT_ID)
  protected String contactId;

  @DatabaseField(columnName = BODY)
  protected long body;

  @DatabaseField(columnName = DATETIME)
  protected long datetime;

  protected Message() {
  }
}
