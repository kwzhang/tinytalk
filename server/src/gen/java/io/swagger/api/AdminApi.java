package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.AdminApiService;
import io.swagger.api.factories.AdminApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.InlineResponse2003;
import io.swagger.model.PriceInformation;

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

@Path("/admin")


@io.swagger.annotations.Api(description = "the admin API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-19T19:21:29.273Z")
public class AdminApi  {
   private final AdminApiService delegate;

   public AdminApi(@Context ServletConfig servletContext) {
      AdminApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("AdminApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (AdminApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = AdminApiServiceFactory.getAdminApi();
      }

      this.delegate = delegate;
   }

    @GET
    @Path("/price")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = InlineResponse2003.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Price Information", response = InlineResponse2003.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response getPrice(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getPrice(xPhoneNumber,xPassword,securityContext);
    }
    @PUT
    @Path("/price")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Void.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response updatePrice(@ApiParam(value = "" ,required=true)@HeaderParam("x-phone-number") String xPhoneNumber
,@ApiParam(value = "" ,required=true)@HeaderParam("x-password") String xPassword
,@ApiParam(value = "" ,required=true) PriceInformation priceInformation
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.updatePrice(xPhoneNumber,xPassword,priceInformation,securityContext);
    }
}
