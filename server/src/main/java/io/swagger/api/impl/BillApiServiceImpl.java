package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.BillInformation;

import java.util.List;
import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.designcraft.business.usage.UsageManager;
import com.designcraft.business.user.UserController;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class BillApiServiceImpl extends BillApiService {
    @Override
    public Response bill(String xPhoneNumber, String xPassword,  @NotNull String period, SecurityContext securityContext) throws NotFoundException {
        // do some magic!    
    	UserController userController = new UserController();
    	if(!userController.isExistUser(xPhoneNumber)) {
    		System.out.println("bill: Invaild xPhoneNumber");
    		return Response.status(Response.Status.UNAUTHORIZED).build();
    	}   
    	if(!userController.isPWCorrect(xPhoneNumber, xPassword)) {    
			System.out.println("bill: Invaild Password");
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	    	    	
    	BillInformation billInfo = new BillInformation();
    	UsageManager usageManager = new UsageManager();
    	billInfo.incallTime(usageManager.getIncallHistory(xPhoneNumber, period));
    	billInfo.outcallTime(usageManager.getOutcallHistory(xPhoneNumber, period));
    	billInfo.sendMsgBytes(usageManager.getTextHistory(xPhoneNumber, period));
    	billInfo.calcCost(1, 1, (float)0.01); // IncallRate, OutcallRate, TxtRate     	
        return Response.ok(billInfo).build();
    }
}
