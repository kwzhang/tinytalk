package com.designcraft.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.designcraft.business.txtmsg.TxtMsgController;

public class TestTxtMsgController {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSendMsg() throws IOException {
		TxtMsgController controller = new TxtMsgController();
		List<String> receivers = new ArrayList<String>();
		receivers.add("00011112222");
		receivers.add("00033334444");
		long timestamp = 1528660597;
		controller.sendMsg("00011112222", receivers, "Message from junit", timestamp);
	}

}
