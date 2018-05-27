package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.UpdateuserApiService;
import io.swagger.api.factories.UpdateuserApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

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

@Path("/updateuser")


@io.swagger.annotations.Api(description = "the updateuser API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-27T15:54:36.606Z")
public class UpdateuserApi  {
   private final UpdateuserApiService delegate;

   public UpdateuserApi(@Context ServletConfig servletContext) {
      UpdateuserApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("UpdateuserApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (UpdateuserApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = UpdateuserApiServiceFactory.getUpdateuserApi();
      }

      this.delegate = delegate;
   }

    @POST
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update user", notes = "This can only be done by the logged in user.", response = Void.class, tags={ "updateuser", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response updateUser(@ApiParam(value = "Updated user object" ,required=true) User body
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.updateUser(body,securityContext);
    }
}
