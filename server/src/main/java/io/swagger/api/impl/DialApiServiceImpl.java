package io.swagger.api.impl;

import java.io.IOException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.designcraft.business.call.CallConfig;
import com.designcraft.business.call.CallController;
import com.designcraft.business.user.UserController;

import io.swagger.api.APILogger;
import io.swagger.api.ApiResponseMessage;
import io.swagger.api.DialApiService;
import io.swagger.api.NotFoundException;
import io.swagger.model.CodecTransport;
import io.swagger.model.CodecTransport.CodecEnum;
import io.swagger.model.CodecTransport.TransportEnum;
import io.swagger.model.DialRequest;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class DialApiServiceImpl extends DialApiService {
    @Override
    public Response callDial(String xPhoneNumber, String xPassword, DialRequest dialRequest, SecurityContext securityContext) throws NotFoundException {
        APILogger.request("Call Dial", dialRequest);
    	UserController userController = new UserController();
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("callDial: Invaild xPhoneNumber");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("callDial: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	
    	CallController controller = new CallController();
    	try {
			controller.dial(xPhoneNumber, dialRequest.getReceiver(), dialRequest.getAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	CallConfig callConfig = new CallConfig();
    	CodecTransport codecTransport = new CodecTransport().codec(CodecEnum.fromValue(callConfig.getCodec()))
    			.transport(TransportEnum.fromValue(callConfig.getTransport()));
    	APILogger.response("Call Dial", codecTransport);
        return Response.ok().entity(codecTransport).build();
    }
    @Override
    public Response callDrop(String xPhoneNumber, String xPassword, SecurityContext securityContext) throws NotFoundException {
    	APILogger.request("Call Drop", "");
    	UserController userController = new UserController();
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("callDrop: Invaild xPhoneNumber");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("callDrop: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	CallController controller = new CallController();
    	try {
			controller.drop(xPhoneNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
