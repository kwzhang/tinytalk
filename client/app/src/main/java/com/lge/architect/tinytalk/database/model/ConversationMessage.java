package com.lge.architect.tinytalk.database.model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@DatabaseTable(tableName = ConversationMessage.TABLE_NAME)
public class ConversationMessage extends DatabaseModel {
  public static final String TABLE_NAME = "conversation_message";
  public static final String ACTION_REFRESH = "ACTION_REFRESH";

  public static final String CONVERSATION_ID = "conversation_id";
  public static final String CONTACT_ID = "contact_id";
  public static final String BODY = "body";
  public static final String DATETIME = "datetime";
  public static final String UNREAD = "unread";

  @DatabaseField(columnName = CONVERSATION_ID)
  protected long conversationId;

  @DatabaseField(columnName = CONTACT_ID)
  protected long contactId;

  @DatabaseField(columnName = BODY)
  protected String body;

  @DatabaseField(columnName = DATETIME, dataType = DataType.DATE_STRING, format = DATETIME_FORMAT)
  protected Date date;

  @DatabaseField(columnName = UNREAD)
  protected boolean unread;

  protected ConversationMessage() {
  }

  public ConversationMessage(long conversationId, long contactId, String body, DateTime datetime, boolean unread) {
    this.conversationId = conversationId;
    this.contactId = contactId;
    this.body = body;
    this.date = datetime.toDate();
    this.unread = unread;
  }

  public String getBody() {
    return (body != null) ? body : "";
  }

  public String getDateTime() {
    return dateTimeFormatter.print(new DateTime(date));
  }

  public long getTimestamp() {
    return new DateTime(date).getMillis();
  }

  public long getContactId() {
    return contactId;
  }

  public void markUnread(boolean unread) {
    this.unread = unread;
  }

  public static List<ConversationMessage> getUnreadMessages(Dao<ConversationMessage, Long> dao, long conversationId) {
    try {
      QueryBuilder<ConversationMessage, Long> builder = dao.queryBuilder();
      builder.where().eq(ConversationMessage.CONVERSATION_ID, new SelectArg(conversationId)).and()
          .eq(ConversationMessage.UNREAD, new SelectArg(true));

      return builder.query();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return Collections.emptyList();
  }

  public static void markToAllRead(Dao<ConversationMessage, Long> dao, long conversationId) {
    List<ConversationMessage> unreadMessages = getUnreadMessages(dao, conversationId);

    try {
      for (ConversationMessage message : unreadMessages) {
        message.markUnread(false);
        dao.update(message);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static ConversationMessage getMessage(Dao<ConversationMessage, Long> dao, long messageId) {
    QueryBuilder<ConversationMessage, Long> queryBuilder = dao.queryBuilder();

    ConversationMessage message = null;
    try {
      queryBuilder.where().eq(ConversationMessage._ID, new SelectArg(messageId));
      if (queryBuilder.countOf() == 1) {
        message = queryBuilder.queryForFirst();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return message;
  }

  public static void markAsRead(Dao<ConversationMessage, Long> dao, long messageId) {
    try {
      ConversationMessage message = getMessage(dao, messageId);

      message.markUnread(false);
      dao.update(message);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
