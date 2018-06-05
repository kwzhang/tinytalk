package com.designcraft.db;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.KeyValueDB;
import com.designcraft.infra.db.redis.RedisDBFactory;

public class RedisKeyValueDBTest {
	private KeyValueDB keyValueDB;

	@Before
	public void setUp() throws Exception {
		AbstractDBFactory dbFactory = new RedisDBFactory();
		keyValueDB = dbFactory.createKeyValueDB();
	}

	@Test
	public void testAddGet() {
		keyValueDB.add("user", "user@gmail.com", "firstName", "Bojun");
		assertEquals(keyValueDB.get("user", "user@gmail.com", "firstName"), "Bojun");
	}
}
