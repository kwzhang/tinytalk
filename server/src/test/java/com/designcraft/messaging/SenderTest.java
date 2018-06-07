package com.designcraft.messaging;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.designcraft.infra.messaging.MessageSender;
import com.designcraft.infra.messaging.mqtt.MqttSender;

public class SenderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSendMessage() {
		MessageSender sender = new MqttSender();
		List<String> receivers = new ArrayList<String>();
		receivers.add("111");
		receivers.add("222");
		receivers.add("333");
		sender.sendMessage(receivers, "Hello world from Java");
	}

}
