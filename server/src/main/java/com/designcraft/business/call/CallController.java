package com.designcraft.business.call;

import java.io.IOException;

import com.designcraft.business.usage.UsageManager;
import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.KeyValueDB;
import com.designcraft.infra.db.redis.RedisDBFactory;
import com.designcraft.infra.messaging.MessageBody;
import com.designcraft.infra.messaging.MessageSender;
import com.designcraft.infra.messaging.MessageTemplate;
import com.designcraft.infra.messaging.jackson.JacksonMessageBody;
import com.designcraft.infra.messaging.mqtt.MqttSender;

public class CallController {
	AbstractDBFactory dbFactory;
	KeyValueDB keyValueDb;
	MessageBody messageBody;
	MessageSender msgSender;
	
	public CallController() {
		dbFactory = new RedisDBFactory();
		keyValueDb = dbFactory.createKeyValueDB();
		messageBody = new JacksonMessageBody();
		msgSender = new MqttSender();
	}
	
	public void dial(String sender, String receiver, String address) throws IOException {
		// DB를 사용한 통화중 체크
//		if (keyValueDb.get("CALL", receiver, "RECEIVER") != null || keyValueDb.get("CALL", receiver, "SENDER") != null) {
//			sendDialResponse(sender, "busy", null);
//		}
		
		Dial dial = new Dial(sender, address);
		MessageTemplate template = new MessageTemplate("dial", dial);
		String messageJson = messageBody.makeMessageBody(template);
		// send message
		msgSender.sendMessage(receiver, messageJson);
		
		// write to db call info
		writeCallInfo(sender, receiver);
	}
	
	public void dialResponse(String receiver, String response, String address) throws IOException {
		String sender = keyValueDb.get("CALL",  receiver, "SENDER");
		
		if (sender == null) {
			System.err.println("Cannot find dial sender!! : RECEIVER=" + receiver);
			return;
		}
		sendDialResponse(sender, response, address);
		
		if (response.equalsIgnoreCase("accept")) {
			new UsageManager().callStart(sender);
		}
	}

	private void sendDialResponse(String sender, String response, String address) throws IOException {
		DialResponse dialResponse = new DialResponse(response, address);
		MessageTemplate template = new MessageTemplate("dialResponse", dialResponse);
		String messageJson = messageBody.makeMessageBody(template);
		msgSender.sendMessage(sender, messageJson);
		
	}

	private void writeCallInfo(String sender, String receiver) {
		// table이름은 CALL
		// 각 사용자(phonenumber) 별로 현재 통화 정보를 저장
		
		// e.g 111이 222에게 전화한 상황이라면
		// 111 사용자의 통화 정보 --> 이 사용자의 RECEVIER는 222야 라는 정보 저장
		keyValueDb.add("CALL", sender, "RECEIVER", receiver);
		// 222 사용자의 통화 정보 --> 이 사용자의 SENDER는 111야 라는 정보 저장
		keyValueDb.add("CALL", receiver, "SENDER", sender);
	}

	public void drop(String phoneNumber) throws IOException {
		// for call history
		String sender = null, receiver = null;
		String callPartner = keyValueDb.get("CALL", phoneNumber, "SENDER");
		if (callPartner == null) {
			callPartner = keyValueDb.get("CALL", phoneNumber, "RECEIVER");
		}
		else {
			sender = callPartner;
			receiver = phoneNumber;
		}
		if (callPartner == null) {
			System.err.println("Cannot find call partner for " + phoneNumber);
			return;
		}
		else {
			sender = phoneNumber;
			receiver = callPartner;
		}
		
		MessageTemplate template = new MessageTemplate("callDrop", "");
		String messageJson = messageBody.makeMessageBody(template);
		msgSender.sendMessage(callPartner, messageJson);
		
		new UsageManager().dropCall(sender, receiver);
	}
}
