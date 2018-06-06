package com.lge.architect.tinytalk.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@DatabaseTable(tableName = ConversationGroup.TABLE_NAME)
public class ConversationGroup extends DatabaseModel {
  public static final String TABLE_NAME = "conversation_group";

  public static final String NAME = "name";

  @DatabaseField(columnName = NAME)
  protected String name;

  protected ConversationGroup() {
  }

  public ConversationGroup(String... memberNames) {
    name = Stream.of(memberNames).collect(Collectors.joining(", "));
  }

  public String getName() {
    return name;
  }
}
