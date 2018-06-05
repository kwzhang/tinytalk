package com.designcraft.infra.db;

public interface AbstractDBFactory {
	public KeyValueDB createKeyValueDB();
	public SetDB createSetDB();
}
