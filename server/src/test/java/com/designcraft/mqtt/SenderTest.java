package com.designcraft.mqtt;

import org.junit.Before;
import org.junit.Test;

import com.designcraft.messaging.MessageSender;
import com.designcraft.messaging.mqtt.MqttSender;

public class SenderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSendMessage() {
		MessageSender sender;
		sender = new MqttSender();
		sender.sendMessage("111", "Message from junit");
	}

}
