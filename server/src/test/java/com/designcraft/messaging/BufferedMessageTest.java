package com.designcraft.messaging;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Before;
import org.junit.Test;

import com.designcraft.infra.messaging.MessageSender;
import com.designcraft.infra.messaging.mqtt.MqttSender;

public class BufferedMessageTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws MqttException {
		MessageSender msgSender = new MqttSender();
		msgSender.sendMessage("foo", "No retry");
		msgSender.sendMessage("foo", "retry", true);
	}

}
