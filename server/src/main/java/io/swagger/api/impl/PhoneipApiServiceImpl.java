package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.PhoneIp;
import io.swagger.model.UpdatePhoneIp;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.phone.PhoneController;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T14:27:00.551Z")
public class PhoneipApiServiceImpl extends PhoneipApiService {
    @Override
    public Response phoneip(String phonenumber, SecurityContext securityContext) throws NotFoundException {
    	// do some magic!
    	System.out.println("phonenumber:" + phonenumber.toString());
    	PhoneController controller = new PhoneController();
    	PhoneIp ip = new PhoneIp();
    	ip.setIp(controller.getIp(phonenumber));
        return Response.ok().entity(ip).build();
    }
    @Override
    public Response updatephoneip(UpdatePhoneIp updatePhoneIp, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(updatePhoneIp.toString());
    	PhoneController controller = new PhoneController();
    	controller.setIp(updatePhoneIp.getPhonenumber(), updatePhoneIp.getIp());
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
