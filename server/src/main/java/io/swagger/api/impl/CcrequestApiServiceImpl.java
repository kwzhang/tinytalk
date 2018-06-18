package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.CCRequestInformation;
import io.swagger.api.NotFoundException;

import java.io.IOException;

import com.designcraft.business.cc.CcController;
import com.designcraft.business.txtmsg.TxtMsgController;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class CcrequestApiServiceImpl extends CcrequestApiService {
    @Override
    public Response ccRequest(String xPhoneNumber, String xPassword, CCRequestInformation ccrequest, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(ccrequest.toString());
    	
    	CcController ccController = new CcController();
    	ccController.create(ccrequest, xPhoneNumber);
    	String inviteMsg = ccController.makeInvitationMsg();

		TxtMsgController controller = new TxtMsgController();
		try {
			controller.sendMsg(xPhoneNumber, ccrequest.getMembers(), inviteMsg, System.currentTimeMillis());
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		
		ccController.setStartTask();	// send start message

        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
