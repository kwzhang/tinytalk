package com.lge.architect.tinytalk.database.model;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

@DatabaseTable(tableName = Contact.TABLE_NAME)
public class Contact extends DatabaseModel {
  public static final String TABLE_NAME = "contact";
  public static final String NAME = "name";
  public static final String PHONE_NUMBER = "phone_number";

  public static final long UNKNOWN_ID = -1;

  @DatabaseField(columnName = NAME)
  protected String name;

  @DatabaseField(columnName = PHONE_NUMBER)
  protected String phoneNumber;

  public static final String[] PROJECTION = new String[]{
      _ID,
      NAME,
      PHONE_NUMBER
  };

  protected Contact() {
  }

  public Contact(String name, String phoneNumber) {
    this.name = name;
    this.phoneNumber = phoneNumber;
  }

  public String getName() {
    return name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public String toString() {
    if (TextUtils.isEmpty(name)) {
      if (TextUtils.isEmpty(phoneNumber)) {
        return "Unknown"; // FIXME: strings.xml
      }
      return phoneNumber;
    }

    return name;
  }

  public static Contact getContact(Dao<Contact, Long> dao, long contactId) {
    QueryBuilder<Contact, Long> contactQueryBuilder = dao.queryBuilder();
    try {
      contactQueryBuilder.where().eq(Contact._ID, new SelectArg(contactId));
      return contactQueryBuilder.queryForFirst();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static Contact getContact(Dao<Contact, Long> contactDao, String number) {
    QueryBuilder<Contact, Long> contactQueryBuilder = contactDao.queryBuilder();

    Contact contact = null;
    try {
      contactQueryBuilder.where().eq(Contact.PHONE_NUMBER, new SelectArg(number));
      if (contactQueryBuilder.countOf() == 1) {
        contact = contactQueryBuilder.queryForFirst();
      } else {
        contact = contactDao.createIfNotExists(new Contact("", number));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return contact;
  }
}
