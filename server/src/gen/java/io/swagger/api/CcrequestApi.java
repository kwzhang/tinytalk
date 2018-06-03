package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.CcrequestApiService;
import io.swagger.api.factories.CcrequestApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.CCRequestInformation;

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

@Path("/ccrequest")


@io.swagger.annotations.Api(description = "the ccrequest API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class CcrequestApi  {
   private final CcrequestApiService delegate;

   public CcrequestApi(@Context ServletConfig servletContext) {
      CcrequestApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("CcrequestApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (CcrequestApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = CcrequestApiServiceFactory.getCcrequestApi();
      }

      this.delegate = delegate;
   }

    @POST
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Send request for conference call", notes = "", response = Void.class, tags={ "ccRequest", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response ccRequest(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@ApiParam(value = "Conference call request" ,required=true) CCRequestInformation ccrequest
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.ccRequest(xPhoneNumber,xPassword,ccrequest,securityContext);
    }
}
