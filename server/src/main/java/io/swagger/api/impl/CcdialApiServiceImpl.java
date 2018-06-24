package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.InlineResponse200;

import io.swagger.api.NotFoundException;

import java.io.IOException;
import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.business.cc.CcController;
import com.designcraft.business.user.UserController;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class CcdialApiServiceImpl extends CcdialApiService {
    @Override
    public Response callCcDial(String xPhoneNumber, String xPassword, String ccnumber, Ip ip, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println("[ccCall] ccnumber: " + ccnumber);
    	System.out.println(ip);
    	
    	UserController userController = new UserController();
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("callCcDial: Invaild xPhoneNumber");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("callCcDial: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	CcController ccController = new CcController();
    	CCJoinedIps ips;
    	
    	try {
    		ips = ccController.startCall(ccnumber, xPhoneNumber, ip.getIp());
    	} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
    	
    	return Response.ok(ips).build();
    }
    @Override
    public Response dropCcDial(String xPhoneNumber, String xPassword, String ccnumber, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println("[ccDropCall] ccnumber: " + ccnumber + ", sender: " + xPhoneNumber);
    	
    	UserController userController = new UserController();
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("dropCcDial: Invaild xPhoneNumber");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("dropCcDial: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	CcController ccController = new CcController();
    	try {
    		ccController.endCall(ccnumber, xPhoneNumber);
    	} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
    	
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
