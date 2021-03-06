package com.designcraft.db;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.SetDB;
import com.designcraft.infra.db.redis.RedisDBFactory;

public class RedisSetDBTest {

	private SetDB setDB;

	@Before
	public void setUp() throws Exception {
		AbstractDBFactory dbFactory = new RedisDBFactory();
		setDB = dbFactory.createSetDB();
	}

	@Test
	public void testAddContains() {
		setDB.add("phonenumber", "1111");
		assertTrue(setDB.contains("phonenumber", "1111"));
	}

}
