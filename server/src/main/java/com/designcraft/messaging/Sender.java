package com.designcraft.messaging;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Sender {
	private static final String CONNECTION = "tcp://localhost:1883";
	private MqttClient mqttClient;
	private String reciever;

	public Sender(String receiver) throws MqttException {
		this.mqttClient = new MqttClient(CONNECTION, MqttClient.generateClientId());
		this.mqttClient.connect();
		this.reciever = receiver;
	}
	
	public void sendMessage(String message) throws MqttException {
		MqttMessage mqttMessage = new MqttMessage();
		mqttMessage.setPayload(message.getBytes());
		mqttClient.publish(this.reciever, mqttMessage);
		mqttClient.disconnect();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
