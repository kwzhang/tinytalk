package com.designcraft.infra.db.redis;

import java.util.Set;

import com.designcraft.infra.db.SetDB;

import redis.clients.jedis.Jedis;

public class RedisSetDB implements SetDB {
	private Jedis jedis;
	
	public RedisSetDB() {
		jedis.connect();
		jedis.auth("designcraft12#$");
		jedis.flushAll();
	}
	
	@Override
	public void add(String table, String key) {
		jedis.sadd(table, key);
	}

	@Override
	public boolean contains(String table, String key) {
		return jedis.sismember(table, key);
	}
	
	@Override
	public void del(String table, String key) {
		jedis.srem(table, key);
	}

	@Override
	public Set<String> get(String table) {
		return jedis.smembers(table);
	}
}
