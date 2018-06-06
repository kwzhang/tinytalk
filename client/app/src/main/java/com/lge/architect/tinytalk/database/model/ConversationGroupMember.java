package com.lge.architect.tinytalk.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = ConversationGroupMember.TABLE_NAME)
public class ConversationGroupMember extends DatabaseModel {
  public static final String TABLE_NAME = "conversation_group_member";

  public static final String GROUP_ID = "group_id";
  public static final String CONTACT_ID = "contact_id";

  @DatabaseField(columnName = GROUP_ID)
  protected long groupId;

  @DatabaseField(columnName = CONTACT_ID)
  protected long contactId;

  protected ConversationGroupMember() {
  }

  public ConversationGroupMember(long groupId, long contactId) {
    this.groupId = groupId;
    this.contactId = contactId;
  }

  public long getGroupId() {
    return groupId;
  }
}
