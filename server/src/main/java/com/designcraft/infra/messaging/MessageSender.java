package com.designcraft.infra.messaging;

import java.util.List;

public abstract class MessageSender {
	public void sendMessage(List<String> receivers, String message) {
		for (String receiver : receivers) {
			sendMessage(receiver, message);
		}
	}
	public abstract void sendMessage(String receiver, String message);
}
