package io.swagger.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.designcraft.messaging.Sender;

import io.swagger.api.ApiResponseMessage;
import io.swagger.api.NotFoundException;
import io.swagger.api.TxtmsgApiService;
import io.swagger.model.TxtMsgRequest;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-27T15:54:36.606Z")
public class TxtmsgApiServiceImpl extends TxtmsgApiService {
    @Override
    public Response txtMsg(TxtMsgRequest body, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(body.toString());
    	try {
			Sender sender = new Sender(body.getReciever());
			sender.sendMessage(body.getMsg());
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
