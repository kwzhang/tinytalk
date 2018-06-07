package io.swagger.api.impl;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.designcraft.business.txtmsg.TxtMsgController;
import com.designcraft.infra.messaging.MessageSender;
import com.designcraft.infra.messaging.mqtt.MqttSender;

import io.swagger.api.ApiResponseMessage;
import io.swagger.api.NotFoundException;
import io.swagger.api.TxtmsgApiService;
import io.swagger.model.TxtMsgRequest;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class TxtmsgApiServiceImpl extends TxtmsgApiService {
	@Override
	public Response txtMsg(String xPhoneNumber, String xPassword, TxtMsgRequest body, SecurityContext securityContext) throws NotFoundException {
		// do some magic!
		System.out.println(body.toString());
		List<String> receivers = body.getReceivers();
		receivers.add(xPhoneNumber);
		TxtMsgController controller = new TxtMsgController();
		controller.sendMsg(xPhoneNumber, receivers, body.getMsg());
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}
}
