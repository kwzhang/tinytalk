package com.designcraft.messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.designcraft.infra.messaging.MessageBody;
import com.designcraft.infra.messaging.jackson.JacksonMessageBody;

class Msg {
	private String sender;
	private List<String> receivers;
	String msg;
	
	// json에 포함될 필드는 반드시 getter를 가져야 함
	public String getSender() {
		return sender;
	}

	// json에 포함될 필드는 반드시 getter를 가져야 함
	public List<String> getReceivers() {
		return receivers;
	}

	// json에 포함될 필드는 반드시 getter를 가져야 함
	public String getMsg() {
		return msg;
	}
	
	public Msg() {
		sender = "00011112222";
		receivers = new ArrayList<String>();
		receivers.add("00011112222");
		receivers.add("00022223333");
		
		msg = "Hello";
	}
}

public class MessageBodyTest {
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws IOException {
		Msg msg = new Msg();
		MessageBody messageBody = new JacksonMessageBody();
		String jsonInString = messageBody.makeMessageBody(msg);
		
		System.out.println(jsonInString);
	}

}
