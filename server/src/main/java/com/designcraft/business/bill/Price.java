package com.designcraft.business.bill;

import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.KeyValueDB;
import com.designcraft.infra.db.redis.RedisDBFactory;

public class Price {
	private AbstractDBFactory dbFactory;
	private KeyValueDB keyValueDB;
	private static final String TABLE = "PRICE";
	
	
	public Price() {
		dbFactory = new RedisDBFactory();
		keyValueDB = dbFactory.createKeyValueDB();
	}
	
	public float getIncallPrice(String period) {
		return Float.parseFloat(keyValueDB.get(TABLE, period, "incall"));
	}
	
	public float getOutcallPrice(String period) {
		return Float.parseFloat(keyValueDB.get(TABLE, period, "outcall"));
	}
	
	public float getMsgPrice(String period) {
		return Float.parseFloat(keyValueDB.get(TABLE, period, "txtmsg"));
	}
	
}
