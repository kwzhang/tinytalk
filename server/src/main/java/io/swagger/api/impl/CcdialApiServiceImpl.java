package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;
import io.swagger.model.CCDialResponse.CodecEnum;
import io.swagger.model.CCDialResponse.TransportEnum;
import io.swagger.model.InlineResponse200;
import io.swagger.api.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.business.call.CallConfig;
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
    	APILogger.request("Conference Call Dial", "CC Number: " + ccnumber, ip);
    	UserController userController = new UserController();
    	try {
    		if(!userController.isExistUser(xPhoneNumber)) {
    			System.out.println("callCcDial: Invaild xPhoneNumber");
    			return Response.status(Response.Status.UNAUTHORIZED).build();
    		}
    		if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
    			System.out.println("callCcDial: Invaild Password");
    			return Response.status(Response.Status.UNAUTHORIZED).build();
    		}
    	} catch (Exception e1) {
    		e1.printStackTrace();
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}   
    	
    	List<String> ips = new ArrayList<String>();
    	CcController ccController = new CcController();
    	try {
    		ips = ccController.startCall(ccnumber, xPhoneNumber, ip.getIp());
    	} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
    	
		CallConfig callConfig = new CallConfig();    	
    	CCDialResponse res = new CCDialResponse().ccJoinedIps(ips).codec(CodecEnum.fromValue(callConfig.getCodec()))
    			.transport(TransportEnum.fromValue(callConfig.getTransport()));

    	APILogger.response("Conference Call Dial", res);
    	return Response.ok(res).build();
    }
    @Override
    public Response dropCcDial(String xPhoneNumber, String xPassword, String ccnumber, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println("[ccDropCall] ccnumber: " + ccnumber + ", sender: " + xPhoneNumber);
    	
    	UserController userController = new UserController();
    	try {
    		if(!userController.isExistUser(xPhoneNumber)) {
    			System.out.println("dropCcDial: Invaild xPhoneNumber");
    			return Response.status(Response.Status.UNAUTHORIZED).build();
    		}
    		if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
    			System.out.println("dropCcDial: Invaild Password");
    			return Response.status(Response.Status.UNAUTHORIZED).build();
    		}
    	} catch (Exception e1) {
    		e1.printStackTrace();
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
