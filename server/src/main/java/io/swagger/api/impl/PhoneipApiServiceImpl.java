package io.swagger.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import io.swagger.api.NotFoundException;
import io.swagger.api.PhoneipApiService;
import io.swagger.model.PhoneIp;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-27T15:54:36.606Z")
public class PhoneipApiServiceImpl extends PhoneipApiService {
    @Override
    public Response phoneip(String phonenumber, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println("phonenumber:" + phonenumber.toString());
//        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    	PhoneIp ip = new PhoneIp();
    	ip.setIp("192.168.0.100");
        return Response.ok().entity(ip).build();
    }
}
