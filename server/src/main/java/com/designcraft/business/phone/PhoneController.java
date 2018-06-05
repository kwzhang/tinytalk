package com.designcraft.business.phone;

import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.KeyValueDB;
import com.designcraft.infra.db.redis.RedisDBFactory;

public class PhoneController {
	private KeyValueDB phoneTable;
	private final String TABLE_NAME = "phone";
	
	public PhoneController () {
		AbstractDBFactory dbFactory = new RedisDBFactory();
		phoneTable = dbFactory.createKeyValueDB();
	}
	
	public String getIp(String phoneNumber) {
		return phoneTable.get(TABLE_NAME, phoneNumber, "ip");
	}
	
	public void setIp(String phoneNumber, String ip) {
		phoneTable.add(TABLE_NAME, phoneNumber, "ip", ip);
	}
}
