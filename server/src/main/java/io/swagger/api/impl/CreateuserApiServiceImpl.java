package io.swagger.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.designcraft.user.UserController;

import io.swagger.api.ApiResponseMessage;
import io.swagger.api.CreateuserApiService;
import io.swagger.api.NotFoundException;
import io.swagger.model.User;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-20T01:55:30.397Z")
public class CreateuserApiServiceImpl extends CreateuserApiService {
    @Override
    public Response createUser(User body, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	UserController.register(body);
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
