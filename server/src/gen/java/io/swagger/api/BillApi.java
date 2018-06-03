package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.BillApiService;
import io.swagger.api.factories.BillApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.BillInformation;

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

@Path("/bill")


@io.swagger.annotations.Api(description = "the bill API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class BillApi  {
   private final BillApiService delegate;

   public BillApi(@Context ServletConfig servletContext) {
      BillApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("BillApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (BillApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = BillApiServiceFactory.getBillApi();
      }

      this.delegate = delegate;
   }

    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Request billing information", notes = "", response = BillInformation.class, tags={ "bill", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = BillInformation.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response bill(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@ApiParam(value = "",required=true) @QueryParam("period") String period
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.bill(xPhoneNumber,xPassword,period,securityContext);
    }
}
