package com.designcraft.business.txtmsg;

import java.util.List;

import com.designcraft.infra.messaging.MessageSender;
import com.designcraft.infra.messaging.mqtt.MqttSender;

public class TxtMsgController {
	private static final String template = "{\"type\": \"txtMsg\",\"sender\": \"[SENDER]\",\"receivers\": [[RECEVIERS]],\"message\": \"[MESSAGE]\"}";
	
	public void sendMsg(String sender, List<String> receivers, String message) {
		MessageSender msgSender = new MqttSender();
		String messageJson = template.replace("[SENDER]", sender);
		StringBuffer sb = new StringBuffer();
		for (String receiver : receivers) {
			sb.append("\""+ receiver + "\",");
		}
		messageJson = messageJson.replace("[RECEVIERS]", sb.substring(0,  sb.length()-1));
		messageJson = messageJson.replace("[MESSAGE]", message);
		
		msgSender.sendMessage(receivers, messageJson);
	}
}
