package com.designcraft.infra.messaging;

import java.util.List;

public abstract class MessageSender {
	public void sendMessage(List<String> receivers, String message) {
		sendMessage(receivers, message, false);
	}
	
	public void sendMessage(List<String> receivers, String message, boolean retryIfFail) {
		for (String receiver : receivers) {
			sendMessage(receiver, message, retryIfFail);
		}
	}
	
	public void sendMessage(String receiver, String message) {
		sendMessage(receiver, message, false);
	}

	public abstract void sendMessage(String receiver, String message, boolean retryIfFail);
	
}
