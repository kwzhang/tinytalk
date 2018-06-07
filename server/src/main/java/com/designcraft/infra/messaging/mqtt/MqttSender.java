package com.designcraft.infra.messaging.mqtt;

import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.designcraft.infra.messaging.MessageSender;

public class MqttSender implements MessageSender {
	private static final String CONNECTION = "tcp://localhost:1883";
	private MqttClient mqttClient;

	public void sendMessage(List<String> receivers, String message) {
		try {
			this.mqttClient = new MqttClient(CONNECTION, MqttClient.generateClientId(), null);
			this.mqttClient.connect();
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setPayload(message.getBytes());
			for (String receiver : receivers) {
				mqttClient.publish(receiver, mqttMessage);
			}
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
