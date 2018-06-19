package com.designcraft.infra.db;

import java.util.Set;

public interface SetDB {
	public void add(String table, String key);
	public boolean contains(String table, String key);
	public void del(String table, String key);
	public Set<String> get(String table);
}
