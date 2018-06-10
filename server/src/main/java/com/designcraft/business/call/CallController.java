package com.designcraft.business.call;

import java.io.IOException;

import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.KeyValueDB;
import com.designcraft.infra.db.redis.RedisDBFactory;
import com.designcraft.infra.messaging.MessageBody;
import com.designcraft.infra.messaging.MessageSender;
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
	
	public void dial(String sender, String receiver) throws IOException {
		if (keyValueDb.get("CALL", receiver, "RECEIVER") != null || keyValueDb.get("CALL", receiver, "SENDER") != null) {
			sendDialResponse(sender, "busy", null);
		}
		
		Dial dial = new Dial(sender);
		String messageJson = messageBody.makeMessageBody(dial);
		// send message
		msgSender.sendMessage(receiver, messageJson);
		
		// write to db call info
		writeCallInfo(sender, receiver);
	}
	
	public void dialResponse(String receiver, String response, String ip) throws IOException {
		String sender = keyValueDb.get("CALL",  receiver, "SENDER");
		if (sender == null) {
			System.err.println("Cannot find dial sender!! : RECEIVER=" + receiver);
			return;
		}
		sendDialResponse(sender, response, ip);
	}

	private void sendDialResponse(String sender, String response, String ip) throws IOException {
		DialResponse dialResponse = new DialResponse(response, ip);
		String messageJson = messageBody.makeMessageBody(dialResponse);
		msgSender.sendMessage(sender, messageJson);
		
	}

	private void writeCallInfo(String sender, String receiver) {
		// table이름은 CALL
		// 각 사용자(phonenumber) 별로 현재 통화 정보를 저장
		
		// e.g 111이 222에게 전화한 상황이라면
		// 111 사용자의 통화 정보 --> 이 사용자의 RECEVIER는 222야 라는 정보 저장
		keyValueDb.add("CALL", sender, "RECEVIER", receiver);
		// 222 사용자의 통화 정보 --> 이 사용자의 SENDER는 111야 라는 정보 저장
		keyValueDb.add("CALL", receiver, "SENDER", sender);
	}
}
