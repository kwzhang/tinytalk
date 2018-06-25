package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;
import io.swagger.model.CodecTransport.CodecEnum;
import io.swagger.model.CodecTransport.TransportEnum;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.IOException;
import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.business.call.CallConfig;
import com.designcraft.business.call.CallController;
import com.designcraft.business.user.UserController;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class DialresponseApiServiceImpl extends DialresponseApiService {
    @Override
    public Response dialResponse(String xPhoneNumber, String xPassword, String response, PhoneAddress phoneAddress, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println("[Call Response]");
    	System.out.println(phoneAddress);
    			
    	UserController userController = new UserController();
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("dialResponse: Invaild xPhoneNumber");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("dialResponse: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	CallController controller = new CallController();
    	try {
			controller.dialResponse(xPhoneNumber, response, phoneAddress.getAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	CallConfig callConfig = new CallConfig();
    	CodecTransport codecTransport = new CodecTransport().codec(CodecEnum.fromValue(callConfig.getCodec()))
    			.transport(TransportEnum.fromValue(callConfig.getTransport()));
        return Response.ok().entity(codecTransport).build();
    }
}
