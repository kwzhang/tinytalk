package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.DialresponseApiService;
import io.swagger.api.factories.DialresponseApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.PhoneAddress;

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

@Path("/dialresponse")


@io.swagger.annotations.Api(description = "the dialresponse API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-10T15:50:21.471Z")
public class DialresponseApi  {
   private final DialresponseApiService delegate;

   public DialresponseApi(@Context ServletConfig servletContext) {
      DialresponseApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("DialresponseApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (DialresponseApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = DialresponseApiServiceFactory.getDialresponseApi();
      }

      this.delegate = delegate;
   }

    @POST
    @Path("/{response}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Response of call receiver", notes = "", response = Void.class, tags={ "dialResponse", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response dialResponse(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@ApiParam(value = "",required=true, allowableValues="accept, busy, deny") @PathParam("response") String response
,@ApiParam(value = "User ip" ,required=true) PhoneAddress phoneIp
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.dialResponse(xPhoneNumber,xPassword,response,phoneIp,securityContext);
    }
}
