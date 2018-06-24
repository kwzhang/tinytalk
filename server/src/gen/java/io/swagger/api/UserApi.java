package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.UserApiService;
import io.swagger.api.factories.UserApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.CardNumber;
import io.swagger.model.InlineResponse200;
import io.swagger.model.NewPasswordInfo;
import io.swagger.model.PhoneNumber;
import io.swagger.model.User;

import java.util.Map;
import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/user")


@io.swagger.annotations.Api(description = "the user API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-18T18:12:22.708Z")
public class UserApi  {
   private final UserApiService delegate;

   public UserApi(@Context ServletConfig servletContext) {
      UserApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("UserApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (UserApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = UserApiServiceFactory.getUserApi();
      }

      this.delegate = delegate;
   }

    @PUT
    @Path("/password")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "change user password", notes = "This can only be done by the logged in user.", response = Void.class, tags={ "changePassword", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response changePassword(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@ApiParam(value = "User phone number to change password" ,required=true) NewPasswordInfo newPasswordInfo
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.changePassword(xPhoneNumber,xPassword,newPasswordInfo,securityContext);
    }
    @POST
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create user", notes = "This can only be done by the logged in user.", response = PhoneNumber.class, tags={ "createUser", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = PhoneNumber.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response createUser(@ApiParam(value = "Created user object" ,required=true) User user
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.createUser(user,securityContext);
    }
    @DELETE
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete user for specified phone number", notes = "", response = Void.class, tags={ "deleteUser", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response deleteUser(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.deleteUser(xPhoneNumber,xPassword,securityContext);
    }
    @PUT
    @Path("/resetpassword")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "reset user password", notes = "Request reset password", response = InlineResponse200.class, tags={ "resetPassword", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "New password", response = InlineResponse200.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response resetPassword(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true) CardNumber cardNumber
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.resetPassword(xPhoneNumber,cardNumber,securityContext);
    }
    @PUT
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update user", notes = "This can only be done by the logged in user.", response = Void.class, tags={ "updateUser", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response updateUser(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@ApiParam(value = "Updated user object" ,required=true) User user
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.updateUser(xPhoneNumber,xPassword,user,securityContext);
    }
    
    @POST
    @Path("/login")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = InlineResponse200.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "User Role", response = InlineResponse200.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response login(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.login(xPhoneNumber,xPassword,securityContext);
    }
    
    @GET
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get user information for specified phone number", notes = "", response = User.class, tags={ "getUser", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = User.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response getUser(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getUser(xPhoneNumber,xPassword,securityContext);
    }
}
