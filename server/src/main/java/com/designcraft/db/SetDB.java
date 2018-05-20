package com.designcraft.db;

public interface SetDB {
	public void add(String table, String key);
	public boolean contains(String table, String key);
}
