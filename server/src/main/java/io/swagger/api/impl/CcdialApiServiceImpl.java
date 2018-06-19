package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.InlineResponse200;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.business.user.UserController;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class CcdialApiServiceImpl extends CcdialApiService {
    @Override
    public Response callCcDial(String xPhoneNumber, String xPassword, String ccnumber, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(ccnumber);
    	
    	UserController userController = new UserController();
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("callCcDial: Invaild xPhoneNumber");
    		return Response.status(Response.Status.NOT_FOUND).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("callCcDial: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response dropCcDial(String xPhoneNumber, String xPassword, String ccnumber, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(ccnumber);
    	
    	UserController userController = new UserController();
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("dropCcDial: Invaild xPhoneNumber");
    		return Response.status(Response.Status.NOT_FOUND).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("dropCcDial: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
