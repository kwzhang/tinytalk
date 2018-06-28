package com.designcraft.infra.db;

import com.designcraft.business.encryption.AES;

public abstract class KeyValueDB {
	abstract public String get(String table, String id, String key);
	abstract public void add(String table, String id, String key, String val);
	abstract public void del(String table, String id);
	abstract public boolean isExist(String table, String id);
	public String get(String table, String id, String key, boolean decryption) throws Exception {
		if (decryption) {
			return AES.decrypt(get(table, id, key));
		}
		else return get(table, id, key);
	}
	public void add(String table, String id, String key, String val, boolean encryption) throws Exception {
		if (encryption) {
			add(table, id, key, AES.encrypt(val));
		}
		else add(table, id, key, val);
	}
}
