package com.designcraft.infra.db.redis;

import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.KeyValueDB;
import com.designcraft.infra.db.SetDB;

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
