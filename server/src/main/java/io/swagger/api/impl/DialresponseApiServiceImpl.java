package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;


import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.IOException;
import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.business.call.CallController;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class DialresponseApiServiceImpl extends DialresponseApiService {
    @Override
    public Response dialResponse(String xPhoneNumber, String xPassword, String response, PhoneAddress phoneAddress, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	
    	
    	CallController controller = new CallController();
    	try {
			controller.dialResponse(xPhoneNumber, response, phoneAddress.getAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
