package com.lge.architect.tinytalk.database.model;

import com.j256.ormlite.field.DatabaseField;

public abstract class DatabaseModel {
  public static final String _ID = "_id";

  @DatabaseField(generatedId = true, columnName = _ID)
  protected transient long id;
}
