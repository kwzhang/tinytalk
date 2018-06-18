package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.InlineResponse200;

import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.business.cc.CcController;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class CcdialApiServiceImpl extends CcdialApiService {
    @Override
    public Response callCcDial(String xPhoneNumber, String xPassword, String ccnumber, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(ccnumber);
    	
    	CcController ccController = new CcController();
    	CCDialResponse res = new CCDialResponse();
    	res.setIp(ccController.getMulticastIp(ccnumber));
    	
    	return Response.ok(res).build();
    }
    @Override
    public Response dropCcDial(String xPhoneNumber, String xPassword, String ccnumber, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(ccnumber);
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
