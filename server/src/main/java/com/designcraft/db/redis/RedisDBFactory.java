package com.designcraft.db.redis;

import com.designcraft.db.AbstractDBFactory;
import com.designcraft.db.KeyValueDB;
import com.designcraft.db.SetDB;

public class RedisDBFactory implements AbstractDBFactory {

	@Override
	public KeyValueDB createKeyValueDB() {
		return new RedisKeyValueDB();
	}

	@Override
	public SetDB createSetDB() {
		return new RedisSetDB();
	}

}
