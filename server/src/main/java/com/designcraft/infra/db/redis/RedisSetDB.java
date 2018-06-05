package com.designcraft.infra.db.redis;

import com.designcraft.infra.db.SetDB;

import redis.clients.jedis.Jedis;

public class RedisSetDB implements SetDB {
	private Jedis jedis;
	
	public RedisSetDB() {
		jedis = new Jedis("localhost");
	}
	
	@Override
	public void add(String table, String key) {
		jedis.sadd(table, key);
	}

	@Override
	public boolean contains(String table, String key) {
		return jedis.sismember(table, key);
	}

}
