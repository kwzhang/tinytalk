package com.designcraft.infra.messaging.jackson;

import java.io.IOException;

import com.designcraft.infra.messaging.MessageBody;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonMessageBody implements MessageBody {

	@Override
	public String makeMessageBody(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
}
