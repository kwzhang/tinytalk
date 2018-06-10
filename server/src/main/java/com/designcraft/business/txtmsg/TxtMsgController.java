package com.designcraft.business.txtmsg;

import java.io.IOException;
import java.util.List;

import com.designcraft.infra.messaging.MessageBody;
import com.designcraft.infra.messaging.MessageSender;
import com.designcraft.infra.messaging.jackson.JacksonMessageBody;
import com.designcraft.infra.messaging.mqtt.MqttSender;

public class TxtMsgController {
	static class Msg {
		private String sender;
		private List<String> receivers;
		private String msg;
		private final String type = "txtMsg";

		public String getSender() {
			return sender;
		}

		public List<String> getReceivers() {
			return receivers;
		}

		public String getMsg() {
			return msg;
		}
		
		public String getType() {
			return type;
		}
		
		public Msg(String sender, List<String> receivers, String message) {
			this.sender = sender;
			this.receivers = receivers;
			this.msg = message;
		}
	}
	
	public void sendMsg(String sender, List<String> receivers, String message) throws IOException {
		// make msg body
		Msg msg = new Msg(sender, receivers, message);
		MessageBody messageBody = new JacksonMessageBody();
		String messageJson = messageBody.makeMessageBody(msg);
		
		// send message
		MessageSender msgSender = new MqttSender();
		msgSender.sendMessage(receivers, messageJson);
	}
}
