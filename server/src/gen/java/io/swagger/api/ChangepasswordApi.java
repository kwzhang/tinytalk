package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.ChangepasswordApiService;
import io.swagger.api.factories.ChangepasswordApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.NewPasswordInfo;

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

@Path("/changepassword")


@io.swagger.annotations.Api(description = "the changepassword API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T19:54:34.446Z")
public class ChangepasswordApi  {
   private final ChangepasswordApiService delegate;

   public ChangepasswordApi(@Context ServletConfig servletContext) {
      ChangepasswordApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("ChangepasswordApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (ChangepasswordApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = ChangepasswordApiServiceFactory.getChangepasswordApi();
      }

      this.delegate = delegate;
   }

    @POST
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "change user password", notes = "This can only be done by the logged in user.", response = Void.class, tags={ "changepassword", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response changePassword(@ApiParam(value = "User phone number to change password" ,required=true) NewPasswordInfo newPasswordInfo
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.changePassword(newPasswordInfo,securityContext);
    }
}
