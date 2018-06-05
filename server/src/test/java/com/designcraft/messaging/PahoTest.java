package com.designcraft.messaging;

import org.junit.Before;
import org.junit.Test;

import com.designcraft.messaging.MessageSender;
import com.designcraft.messaging.mqtt.MqttSender;

public class PahoTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		MessageSender sender = new MqttSender();
		sender.sendMessage("111", "Hello world from Java");
	}

}
