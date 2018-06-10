package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.DialApiService;
import io.swagger.api.factories.DialApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.DialRequest;

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

@Path("/dial")


@io.swagger.annotations.Api(description = "the dial API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class DialApi  {
   private final DialApiService delegate;

   public DialApi(@Context ServletConfig servletContext) {
      DialApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("DialApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (DialApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = DialApiServiceFactory.getDialApi();
      }

      this.delegate = delegate;
   }

    @POST
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Dial voice call", notes = "", response = Void.class, tags={ "dial", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response callDial(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@ApiParam(value = "" ,required=true) DialRequest dialRequest
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.callDial(xPhoneNumber,xPassword,dialRequest,securityContext);
    }
    @DELETE
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Drop call", notes = "", response = Void.class, tags={ "callDrop", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response callDrop(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.callDrop(xPhoneNumber,xPassword,securityContext);
    }
}
