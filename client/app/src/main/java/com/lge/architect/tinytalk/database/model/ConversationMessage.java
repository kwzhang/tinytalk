package com.lge.architect.tinytalk.database.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import java.util.Date;

@DatabaseTable(tableName = ConversationMessage.TABLE_NAME)
public class ConversationMessage extends DatabaseModel {
  public static final String TABLE_NAME = "conversation_message";
  public static final String ACTION_REFRESH = "ACTION_REFRESH";

  public static final String CONVERSATION_ID = "conversation_id";
  public static final String CONTACT_ID = "contact_id";
  public static final String BODY = "body";
  public static final String DATETIME = "datetime";

  @DatabaseField(columnName = CONVERSATION_ID)
  protected long conversationId;

  @DatabaseField(columnName = CONTACT_ID)
  protected long contactId;

  @DatabaseField(columnName = BODY)
  protected String body;

  @DatabaseField(columnName = DATETIME, dataType = DataType.DATE_STRING, format = DATETIME_FORMAT)
  protected Date date;

  protected ConversationMessage() {
  }

  public ConversationMessage(long conversationId, long contactId, String body, DateTime datetime) {
    this.conversationId = conversationId;
    this.contactId = contactId;
    this.body = body;
    this.date = datetime.toDate();
  }

  public String getBody() {
    return (body != null) ? body : "";
  }

  public String getDateTime() {
    return dateTimeFormatter.print(new DateTime(date));
  }

  public long getContactId() {
    return contactId;
  }
}
