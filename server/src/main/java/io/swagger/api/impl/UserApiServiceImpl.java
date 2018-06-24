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
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("changePassword: Invaild xPhoneNumber");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("changePassword: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	
    	
    	userController.chnagePW(xPhoneNumber,newPasswordInfo.getNewPassword() );
    	System.out.println(xPhoneNumber +"`s password is changed");
    
    	
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

    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("deleteUser: Invaild xPhoneNumber");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("deleteUser: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

    	userController.deleteUser(xPhoneNumber);
    	System.out.println(xPhoneNumber +"is deleted");
   	

    	
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response updateUser(String xPhoneNumber, String xPassword, User user, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	UserController userController = new UserController();

    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("changePassword: Invaild xPhoneNumber");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("changePassword: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
    	
    	userController.updateUser(xPhoneNumber, user);
    	System.out.println(xPhoneNumber +"is updated");
    	
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
    	User user = new User();
    	if(userController.isAdminUser(xPhoneNumber)) {    		
    		user.setRole("admin");
    		user.setName("admin");
    		user.setEmail("admin@admin.com");    	    		
    		return Response.ok(user).build();      
    	}    	
    	
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("callDrop: Invaild xPhoneNumber");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("callDrop: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}    	
    	
		user = userController.getLoginUserinfo(xPhoneNumber);
		user.setRole("user"); 	
    	
    	
		return Response.ok(user).build();      	  	
        
    }
    
    @Override
    public Response getUser(String xPhoneNumber, String xPassword, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println("get user");
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
