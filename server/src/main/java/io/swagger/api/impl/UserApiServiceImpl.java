package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.PhoneNumber;
import io.swagger.model.User;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.user.UserController;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T14:27:00.551Z")
public class UserApiServiceImpl extends UserApiService {
    @Override
    public Response createUser(User user, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	UserController userController = new UserController();
    	PhoneNumber phoneNumber = new PhoneNumber();
    	phoneNumber.setNumber(userController.register(user));
    	return Response.ok(phoneNumber).build();
    }
    @Override
    public Response deleteuser(String phonenumber, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(phonenumber);
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response updateUser(User user, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(user.toString());
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
