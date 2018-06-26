package com.lge.architect.tinytalk.database.model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@DatabaseTable(tableName = ConversationMember.TABLE_NAME)
public class ConversationMember extends DatabaseModel {
  public static final String TABLE_NAME = "conversation_member";

  public static final String CONVERSATION_ID = "conversation_id";
  public static final String CONTACT_ID = "contact_id";

  @DatabaseField(columnName = CONVERSATION_ID)
  protected long conversationId;

  @DatabaseField(columnName = CONTACT_ID)
  protected long contactId;

  protected ConversationMember() {
  }

  public ConversationMember(long conversationId, long contactId) {
    this.conversationId = conversationId;
    this.contactId = contactId;
  }

  public long getConversationId() {
    return conversationId;
  }

  public long getContactId() {
    return contactId;
  }

  public static List<ConversationMember> getConversationMembers(Dao<ConversationMember, Long> dao, long conversationId) {
    try {
      QueryBuilder<ConversationMember, Long> builder = dao.queryBuilder();
      builder.where().eq(ConversationMember.CONVERSATION_ID, new SelectArg(conversationId));

      return builder.query();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return Collections.emptyList();
  }
}
