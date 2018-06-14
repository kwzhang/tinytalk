package com.designcraft.infra.db;

public interface KeyValueDB {
	public String get(String table, String id, String key);
	public void add(String table, String id, String key, String val);
	public void del(String table, String id);
	

}
