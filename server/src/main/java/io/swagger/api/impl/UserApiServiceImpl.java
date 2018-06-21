package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

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
    	{
    		System.out.println("UserApiServiceImpl.changePassword : invalid password");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
    	
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
    	{
    		System.out.println("UserApiServiceImpl.deleteUser : invalid password");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}
    	
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
    	{
    		System.out.println("UserApiServiceImpl.updateUser : invalid password");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    		//return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    	}
    	
    	System.out.println(user);
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response resetPassword(String xPhoneNumber, CardNumber cardNumber, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	
    	UserController userController = new UserController();
    	if(userController.isUserCardInfoMatched(xPhoneNumber, cardNumber)) { 
    		System.out.println(cardNumber);
    		NewPasswordInfo newPasswordInfo = new NewPasswordInfo();
    		newPasswordInfo.setTempPassword();
    		userController.chnagePW(xPhoneNumber,newPasswordInfo.getNewPassword() );
            return Response.ok(newPasswordInfo).build();        	
    	}
    	    	
    	return Response.serverError().entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "invaild User Infor(Card Number)")).build();
    }
    @Override
    public Response login(String xPhoneNumber, String xPassword, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println("login");
    	UserController userController = new UserController();
    	UserRole userRole = new UserRole();
    	if(userController.isAdminUser(xPhoneNumber)) {
    		userRole.role(UserRole.RoleEnum.ADMIN);
    		return Response.ok(userRole).build();      
    	}
    	
    	
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("callDrop: Invaild xPhoneNumber");
    		return Response.status(Response.Status.NOT_FOUND).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("callDrop: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	 	
    	userRole.role(UserRole.RoleEnum.USER);
		return Response.ok(userRole).build();      	  	
        
    }
}
