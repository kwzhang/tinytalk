package com.designcraft.db;

public interface AbstractDBFactory {
	public KeyValueDB createKeyValueDB();
	public SetDB createSetDB();
}
