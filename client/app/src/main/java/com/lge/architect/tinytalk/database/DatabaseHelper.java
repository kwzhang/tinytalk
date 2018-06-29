package com.lge.architect.tinytalk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.database.model.ConversationMember;
import com.lge.architect.tinytalk.database.model.ConversationMessage;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
  private static final String DATABASE_NAME = "conversations.db";
  private static final int DATABASE_VERSION = 1;

  private Dao<Conversation, Long> conversationDao;
  private Dao<Contact, Long> contactDao;
  private Dao<ConversationMessage, Long> conversationMessageDao;
  private Dao<ConversationMember, Long> conversationMemberDao;

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
    try {
      TableUtils.createTable(connectionSource, Conversation.class);
      TableUtils.createTable(connectionSource, Contact.class);
      TableUtils.createTable(connectionSource, ConversationMessage.class);
      TableUtils.createTable(connectionSource, ConversationMember.class);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
  }

  public void dropAllTables() {
    try {
      TableUtils.clearTable(getContactDao().getConnectionSource(), Contact.class);
      TableUtils.clearTable(getConversationDao().getConnectionSource(), Conversation.class);
      TableUtils.clearTable(getConversationMessageDao().getConnectionSource(), ConversationMessage.class);
      TableUtils.clearTable(getConversationMemberDao().getConnectionSource(), ConversationMember.class);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Dao<Conversation, Long> getConversationDao() {
    if (conversationDao == null) {
      try {
        conversationDao = getDao(Conversation.class);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return conversationDao;
  }

  public Dao<Contact, Long> getContactDao() {
    if (contactDao == null) {
      try {
        contactDao = getDao(Contact.class);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return contactDao;
  }

  public Dao<ConversationMessage, Long> getConversationMessageDao() {
    if (conversationMessageDao == null) {
      try {
        conversationMessageDao = getDao(ConversationMessage.class);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return conversationMessageDao;
  }

  public Dao<ConversationMember, Long> getConversationMemberDao() {
    if (conversationMemberDao == null) {
      try {
        conversationMemberDao = getDao(ConversationMember.class);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return conversationMemberDao;
  }
}
