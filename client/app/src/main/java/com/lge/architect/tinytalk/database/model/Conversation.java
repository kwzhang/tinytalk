package com.lge.architect.tinytalk.database.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DatabaseTable(tableName = Conversation.TABLE_NAME)
public class Conversation extends DatabaseModel {
  public static final String TABLE_NAME = "conversation";

  public static final String GROUP_NAME = "group_id";
  public static final String HASH_CODE = "hash_code";
  public static final String DATETIME = "datetime";

  @DatabaseField(columnName = GROUP_NAME)
  protected String groupName;

  @DatabaseField(columnName = HASH_CODE)
  protected String hashCode;

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

  public Conversation(Collection<Contact> contacts) {
    this(contacts.toArray(new Contact[0]));
  }

  public Conversation(Contact... contacts) {
    this.groupName = TextUtils.join(", ", contacts);
    this.hashCode = generateHashCode(contacts);

    updateToNow();
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(List<Contact> contacts) {
    Collections.sort(contacts);
    this.groupName = TextUtils.join(", ", contacts);
    this.hashCode = generateHashCode(contacts);
  }

  public String getHashCode() {
    return hashCode;
  }

  public void updateToNow() {
    this.datetime = DateTime.now().toDate();
  }

  public static @Nullable Conversation getConversation(Dao<Conversation, Long> dao, Contact... contacts) {
    return getConversation(dao, Stream.of(contacts).map(Contact::getPhoneNumber).collect(Collectors.toSet()));
  }

  public static @Nullable Conversation getConversation(Dao<Conversation, Long> dao, Set<String> participants) {
    String hashCode = Conversation.generateHashCode(participants.toArray(new String[0]));

    try {
      QueryBuilder<Conversation, Long> builder = dao.queryBuilder();
      builder.where().eq(Conversation.HASH_CODE, new SelectArg(hashCode));

      if (builder.countOf() == 1) {
        return builder.queryForFirst();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static @Nullable Conversation getConversation(Dao<Conversation, Long> dao, long conversationId) {
    try {
      QueryBuilder<Conversation, Long> builder = dao.queryBuilder();
      builder.where().eq(Conversation._ID, new SelectArg(conversationId));

      if (builder.countOf() == 1) {
        return builder.queryForFirst();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  private static String generateHashCode(String... numbers) {
    TreeSet<String> set = new TreeSet<>();
    Collections.addAll(set, numbers);
    return sha256(set.toString());
  }

  private static String generateHashCode(Contact... contacts) {
    TreeSet<String> set = new TreeSet<>();
    for (Contact contact : contacts) {
      set.add(contact.getPhoneNumber());
    }
    return sha256(set.toString());
  }

  private static String generateHashCode(List<Contact> contacts) {
    TreeSet<String> set = new TreeSet<>();
    for (Contact contact : contacts) {
      set.add(contact.getPhoneNumber());
    }
    return sha256(set.toString());
  }

  private static String sha256(String data) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(data.getBytes());
      return bytesToHex(md.digest());
    } catch (NoSuchAlgorithmException e) {
      return null;
    }
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder result = new StringBuilder();
    for (byte b : bytes) {
      result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
    }

    return result.toString();
  }
}
