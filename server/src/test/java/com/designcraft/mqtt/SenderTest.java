package com.designcraft.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Before;
import org.junit.Test;

import com.desighcraft.mqtt.Sender;

public class SenderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSendMessage() {
		Sender sender;
		try {
			sender = new Sender("111");
			sender.sendMessage("Message from junit");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
