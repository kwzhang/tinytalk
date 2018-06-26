package com.designcraft.business.txtmsg;

import java.io.IOException;
import java.util.List;

import com.designcraft.business.usage.UsageManager;
import com.designcraft.infra.messaging.MessageBody;
import com.designcraft.infra.messaging.MessageSender;
import com.designcraft.infra.messaging.MessageTemplate;
import com.designcraft.infra.messaging.jackson.JacksonMessageBody;
import com.designcraft.infra.messaging.mqtt.MqttSender;

public class TxtMsgController {
	static class Msg {
		private String sender;
		private List<String> receivers;
		private String message;
		private long timestamp;

		public Msg(String sender, List<String> receivers, String message, long timestamp) {
			this.sender = sender;
			this.receivers = receivers;
			this.message = message;
			this.timestamp = timestamp;
		}
		
		public String getSender() {
			return sender;
		}

		public List<String> getReceivers() {
			return receivers;
		}

		public String getMessage() {
			return message;
		}
		
		public long getTimestamp() {
			return timestamp;
		}
	}
	
	public void sendMsg(String sender, List<String> receivers, String message, long timestamp) throws IOException {
		// make msg body
		Msg msg = new Msg(sender, receivers, message, timestamp);
		MessageTemplate template = new MessageTemplate("txtMsg", msg);
		MessageBody messageBody = new JacksonMessageBody();
		String messageJson = messageBody.makeMessageBody(template);
		
		// send message
		MessageSender msgSender = new MqttSender();
		msgSender.sendMessage(receivers, messageJson, false);
		
		UsageManager usage = new UsageManager();
		usage.txtMsg(sender, message, "SEND");
		for ( String receiver : receivers )
			usage.txtMsg(receiver, message, "RECEIVE");
	}
}
