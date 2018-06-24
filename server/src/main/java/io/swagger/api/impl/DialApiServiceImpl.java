package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.DialRequest;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.IOException;
import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.business.call.CallController;
import com.designcraft.business.user.UserController;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class DialApiServiceImpl extends DialApiService {
    @Override
    public Response callDial(String xPhoneNumber, String xPassword, DialRequest dialRequest, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(dialRequest);
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
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response callDrop(String xPhoneNumber, String xPassword, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println("[Call Drop]");
    	System.out.println("requester: " + xPhoneNumber);
    	
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
