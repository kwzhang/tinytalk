package com.designcraft.infra.db.redis;

import com.designcraft.infra.db.KeyValueDB;

import redis.clients.jedis.Jedis;

public class RedisKeyValueDB implements KeyValueDB {
	private Jedis jedis;
	
	public RedisKeyValueDB() {
		jedis = new Jedis("35.168.51.250");
		jedis.connect();
		jedis.auth("designcraft12#$");
		jedis.flushAll();
	}

	@Override
	public String get(String table, String id, String key) {
		String value = "";
		
		if(jedis.hexists(table+":"+id, key))
			value = jedis.hget(table+":"+id, key);
		else 
			value = "";
		
		return value;
		
		
	}
	
	public boolean isExist(String table, String id) {
		return jedis.exists(table+":"+id);
	}

	@Override
	public void add(String table, String id, String key, String val) {
		jedis.hset(table+":"+id, key, val);
	}
	
	public void del(String table, String id) {
		jedis.del(table+":"+id);
	}
}
