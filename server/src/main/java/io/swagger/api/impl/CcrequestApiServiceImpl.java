package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.CCRequestInformation;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.business.cc.CcController;
import com.designcraft.business.user.UserController;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class CcrequestApiServiceImpl extends CcrequestApiService {
    @Override
    public Response ccRequest(String xPhoneNumber, String xPassword, CCRequestInformation ccrequest, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(ccrequest.toString());
    	
    	UserController userController = new UserController();
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("ccRequest: Invaild xPhoneNumber");
    		return Response.status(Response.Status.NOT_FOUND).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("ccRequest: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
//    	CCRequestInformation ccInfo = new CCRequestInformation();
//    	ccInfo.setMembers(ccrequest.getMembers());
//    	ccInfo.setStartDatetime(ccrequest.getStartDatetime());
//    	ccInfo.setEndDatetime(ccrequest.getEndDatetime());
    	
    	//CcController ccController = new CcController();
    	//ccController.create(ccrequest);
    	

        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
