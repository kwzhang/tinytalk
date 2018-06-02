package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.PhoneipApiService;
import io.swagger.api.factories.PhoneipApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.PhoneIp;
import io.swagger.model.UpdatePhoneIp;

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

@Path("/phoneip")


@io.swagger.annotations.Api(description = "the phoneip API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T19:54:34.446Z")
public class PhoneipApi  {
   private final PhoneipApiService delegate;

   public PhoneipApi(@Context ServletConfig servletContext) {
      PhoneipApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("PhoneipApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (PhoneipApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = PhoneipApiServiceFactory.getPhoneipApi();
      }

      this.delegate = delegate;
   }

    @GET
    @Path("/{phonenumber}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Request phoneip for specified phone number", notes = "", response = PhoneIp.class, tags={ "getphoneip", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = PhoneIp.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response phoneip(@ApiParam(value = "",required=true) @PathParam("phonenumber") String phonenumber
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.phoneip(phonenumber,securityContext);
    }
    @PUT
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update phoneip for specified phone number", notes = "", response = Void.class, tags={ "updatephoneip", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response updatephoneip(@ApiParam(value = "User phone number and new ip" ,required=true) UpdatePhoneIp updatePhoneIp
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.updatephoneip(updatePhoneIp,securityContext);
    }
}
