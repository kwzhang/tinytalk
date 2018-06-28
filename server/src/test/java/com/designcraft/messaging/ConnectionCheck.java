package com.designcraft.messaging;

import static org.junit.Assert.*;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Before;
import org.junit.Test;

public class ConnectionCheck {
	private static final String CONNECTION = "tcp://localhost:1883";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws MqttException {
		MqttClient mqttClient;
		mqttClient = new MqttClient(CONNECTION, MqttClient.generateClientId(), new MemoryPersistence());
		mqttClient.connect();
//		mqttClient.
	}

}
