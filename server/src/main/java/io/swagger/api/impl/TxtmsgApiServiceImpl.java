package io.swagger.api.impl;

import java.io.IOException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.designcraft.business.txtmsg.TxtMsgController;
import com.designcraft.business.user.UserController;

import io.swagger.api.APILogger;
import io.swagger.api.ApiResponseMessage;
import io.swagger.api.NotFoundException;
import io.swagger.api.TxtmsgApiService;
import io.swagger.model.TxtMsgRequest;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class TxtmsgApiServiceImpl extends TxtmsgApiService {
	@Override
	public Response txtMsg(String xPhoneNumber, String xPassword, TxtMsgRequest body, SecurityContext securityContext) throws NotFoundException {
		APILogger.request("Text Message", body);
		UserController userController = new UserController();
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("txtMsg: Invaild xPhoneNumber");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("txtMsg: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		TxtMsgController controller = new TxtMsgController();
		try {
			controller.sendMsg(xPhoneNumber, body.getReceivers(), body.getMessage(), body.getTimestamp());
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
	}
}
