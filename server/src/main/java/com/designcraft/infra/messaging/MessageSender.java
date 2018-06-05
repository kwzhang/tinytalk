package com.designcraft.infra.messaging;

public interface MessageSender {
	public void sendMessage(String receiver, String message);
}
