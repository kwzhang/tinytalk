package com.designcraft.business.call;

import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.KeyValueDB;
import com.designcraft.infra.db.redis.RedisDBFactory;

public class CallConfig {
	AbstractDBFactory dbFactory;
	KeyValueDB keyValueDb;
	
	public CallConfig() {
		dbFactory = new RedisDBFactory();
		keyValueDb = dbFactory.createKeyValueDB();
	}
	
	public String getCodec() {
		return keyValueDb.get("ADMIN", "call", "codec");
	}
	
	public String getTransport() {
		return keyValueDb.get("ADMIN", "call", "transport");
	}
}
