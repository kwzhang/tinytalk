package io.swagger.api.impl;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.designcraft.business.bill.Price;
import com.designcraft.business.usage.UsageManager;
import com.designcraft.business.user.UserController;

import io.swagger.api.APILogger;
import io.swagger.api.BillApiService;
import io.swagger.api.NotFoundException;
import io.swagger.model.BillInformation;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class BillApiServiceImpl extends BillApiService {
    @Override
    public Response bill(String xPhoneNumber, String xPassword,  @NotNull String period, SecurityContext securityContext) throws NotFoundException {
        // do some magic!    
    	APILogger.request("Bill", "Billing Period: " + period);
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
    	billInfo.sendMsgBytes(usageManager.getSentTextHistory(xPhoneNumber, period));
    	billInfo.receiveMsgBytes(usageManager.getReceivedTextHistory(xPhoneNumber, period));
    	Price price = new Price();
    	billInfo.calcCost(price.getIncallPrice(period), price.getOutcallPrice(period),
    			price.getReceivedMsgPrice(period), price.getSentMsgPrice(period)); // IncallRate, OutcallRate, ReceivedTxtRate, SentTxtRate     	
    	APILogger.response("Bill", billInfo);
    	return Response.ok(billInfo).build();
    }
}
