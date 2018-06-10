package com.designcraft.infra.messaging;

import java.io.IOException;

public interface MessageBody {
	public String makeMessageBody(Object object) throws IOException;
}
