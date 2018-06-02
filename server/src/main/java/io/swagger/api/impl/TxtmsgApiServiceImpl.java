package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.TxtMsgRequest;
import io.swagger.model.TxtMsgResult;

import java.util.List;
import java.io.InputStream;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.messaging.Sender;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T14:27:00.551Z")
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
