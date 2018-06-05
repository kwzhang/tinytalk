package com.lge.architect.tinytalk.database.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import java.util.Date;

@DatabaseTable(tableName = Conversation.TABLE_NAME)
public class Conversation extends DatabaseModel {
  public static final String TABLE_NAME = "conversation";

  public static final String GROUP_ID = "group_id";
  public static final String DATETIME = "datetime";

  @DatabaseField(columnName = GROUP_ID)
  protected long groupId;

  @DatabaseField(columnName = DATETIME, dataType=DataType.DATE_STRING, format=DATETIME_FORMAT)
  protected Date datetime;

  public static final String[] PROJECTION = new String[] {
      _ID,
      ConversationGroup.NAME,
      ConversationMessage.BODY,
      DATETIME
  };

  protected Conversation() {
  }

  public Conversation(long groupId) {
    this.groupId = groupId;

    updateToNow();
  }

  public void updateToNow() {
    this.datetime = DateTime.now().toDate();
  }
}
