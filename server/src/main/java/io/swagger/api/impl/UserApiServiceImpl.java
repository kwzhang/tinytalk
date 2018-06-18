package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.NewPasswordInfo;
import io.swagger.model.PhoneNumber;
import io.swagger.model.User;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.business.user.UserController;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class UserApiServiceImpl extends UserApiService {
    @Override
    public Response changePassword(String xPhoneNumber, String xPassword, NewPasswordInfo newPasswordInfo, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(newPasswordInfo);
    	UserController userController = new UserController();
    	if(userController.isPWCorrect(xPhoneNumber, xPassword)) {
    		userController.chnagePW(xPhoneNumber,newPasswordInfo.getNewPassword() );
    		System.out.println(xPhoneNumber +"`s password is changed");
    	}
    	else
    		System.out.println("cannot change Password : invalid password");
    	
    	System.out.println(newPasswordInfo.getNewPassword());
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response createUser(User user, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	UserController userController = new UserController();
    	PhoneNumber phoneNumber = new PhoneNumber();
    	phoneNumber.setNumber(userController.register(user));
    	return Response.ok(phoneNumber).build();
    }
    @Override
    public Response deleteUser(String xPhoneNumber, String xPassword, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println("start delete: " + xPhoneNumber);
    	UserController userController = new UserController();
    	if(userController.isPWCorrect(xPhoneNumber, xPassword)) {
    		userController.deleteUser(xPhoneNumber);
    		System.out.println(xPhoneNumber +"is deleted");
    	}
    	else
    		System.out.println("cannot delete : invalid password");
    	
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response updateUser(String xPhoneNumber, String xPassword, User user, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	//UserController userController = new UserController();
    	//PhoneNumber phoneNumber = new PhoneNumber();
    	//phoneNumber.setNumber(userController.register(user));
    	UserController userController = new UserController();
    	if(userController.isPWCorrect(xPhoneNumber, xPassword)) {
    		userController.updateUser(xPhoneNumber, user);
    		System.out.println(xPhoneNumber +"is updated");
    	}
    	else
    		System.out.println("cannot update : invalid password");
    	
    	
    	System.out.println(user);
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
