package com.designcraft.infra.messaging;

import java.util.List;

public interface MessageSender {
	public void sendMessage(List<String> receivers, String message);
}
