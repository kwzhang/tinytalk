package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.CcdialApiService;
import io.swagger.api.factories.CcdialApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.InlineResponse200;

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

@Path("/ccdial")


@io.swagger.annotations.Api(description = "the ccdial API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class CcdialApi  {
   private final CcdialApiService delegate;

   public CcdialApi(@Context ServletConfig servletContext) {
      CcdialApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("CcdialApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (CcdialApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = CcdialApiServiceFactory.getCcdialApi();
      }

      this.delegate = delegate;
   }

    @POST
    @Path("/{ccnumber}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Dial to conference call number", notes = "", response = InlineResponse200.class, tags={ "callCcDial", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "cc ip", response = InlineResponse200.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response callCcDial(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@ApiParam(value = "",required=true) @PathParam("ccnumber") String ccnumber
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.callCcDial(xPhoneNumber,xPassword,ccnumber,securityContext);
    }
    @DELETE
    @Path("/{ccnumber}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Dial to conference call number", notes = "", response = Void.class, tags={ "dropCcDial", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response dropCcDial(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@ApiParam(value = "",required=true) @PathParam("ccnumber") String ccnumber
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.dropCcDial(xPhoneNumber,xPassword,ccnumber,securityContext);
    }
}
