package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.UpdatePhoneIp;

import java.util.List;
import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.business.phone.PhoneController;
import com.designcraft.business.user.UserController;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class PhoneipApiServiceImpl extends PhoneipApiService {
    @Override
    public Response updatePhoneIp(String xPhoneNumber, String xPassword, UpdatePhoneIp updatePhoneIp, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(updatePhoneIp.toString());
		
		UserController userController = new UserController();
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("updatePhoneIp: Invaild xPhoneNumber");
    		return Response.status(Response.Status.NOT_FOUND).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("updatePhoneIp: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	PhoneController controller = new PhoneController();
    	controller.setIp(xPhoneNumber, updatePhoneIp.getIp());
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
