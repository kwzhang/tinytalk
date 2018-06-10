package com.lge.architect.tinytalk.database.model;

import android.text.TextUtils;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import java.util.Date;

@DatabaseTable(tableName = Conversation.TABLE_NAME)
public class Conversation extends DatabaseModel {
  public static final String TABLE_NAME = "conversation";

  public static final String GROUP_NAME = "group_id";
  public static final String DATETIME = "datetime";

  @DatabaseField(columnName = GROUP_NAME)
  protected String groupName;

  @DatabaseField(columnName = DATETIME, dataType=DataType.DATE_STRING, format=DATETIME_FORMAT)
  protected Date datetime;

  public static final String[] PROJECTION = new String[] {
      _ID,
      GROUP_NAME,
      ConversationMessage.BODY,
      DATETIME
  };

  protected Conversation() {
  }

  public Conversation(Contact... contacts) {
    this.groupName = TextUtils.join(", ", contacts);

    updateToNow();
  }

  public void updateToNow() {
    this.datetime = DateTime.now().toDate();
  }
}
