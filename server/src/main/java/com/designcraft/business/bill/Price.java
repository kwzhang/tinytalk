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
		String incallValue = keyValueDB.get(TABLE, period, "incall");
		if(incallValue.equals(""))
			incallValue = "0";
		return Float.parseFloat(incallValue);
	}
	
	public float getOutcallPrice(String period) {
		String outcallValue = keyValueDB.get(TABLE, period, "outcall");
		if(outcallValue.equals(""))
			outcallValue = "0";
		
		return Float.parseFloat(outcallValue);
	}
	
	public float getMsgPrice(String period) {
		String txtmsgValue = keyValueDB.get(TABLE, period, "txtmsg");
		if(txtmsgValue.equals(""))
			txtmsgValue = "0";
		return Float.parseFloat(txtmsgValue);
	}
	
}
