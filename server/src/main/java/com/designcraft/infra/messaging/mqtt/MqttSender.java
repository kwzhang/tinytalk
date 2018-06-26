package com.designcraft.infra.messaging.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.designcraft.infra.messaging.MessageSender;

public class MqttSender extends MessageSender {
//	private static final String CONNECTION = "tcp://35.168.51.250:1883";
	private static final String CONNECTION = "tcp://localhost:1883";
	private MqttClient mqttClient;

	public void sendMessage(String receiver, String message, boolean retryIfFail) {
		try {
			this.mqttClient = new MqttClient(CONNECTION, MqttClient.generateClientId(), new MemoryPersistence());
			this.mqttClient.connect();
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setPayload(message.getBytes());
			if (retryIfFail) {
				mqttMessage.setQos(1);
			}
			else {
				mqttMessage.setQos(0);
			}
			mqttClient.publish(receiver, mqttMessage);
			System.out.println("==========================================");
			System.out.println("MQTT Publish : subscriber = " + receiver);
			System.out.println("------------------------------------------");
			System.out.println(mqttMessage);
		} catch (MqttException e) {
			e.printStackTrace();
		} finally {
			try {
				mqttClient.disconnect();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
