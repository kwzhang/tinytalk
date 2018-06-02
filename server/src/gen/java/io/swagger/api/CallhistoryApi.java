package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.CallhistoryApiService;
import io.swagger.api.factories.CallhistoryApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.CallHistoryRequest;
import io.swagger.model.CallHistoryResponse;

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

@Path("/callhistory")


@io.swagger.annotations.Api(description = "the callhistory API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T19:54:34.446Z")
public class CallhistoryApi  {
   private final CallhistoryApiService delegate;

   public CallhistoryApi(@Context ServletConfig servletContext) {
      CallhistoryApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("CallhistoryApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (CallhistoryApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = CallhistoryApiServiceFactory.getCallhistoryApi();
      }

      this.delegate = delegate;
   }

    @POST
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Request call history for specified phone number", notes = "", response = CallHistoryResponse.class, responseContainer = "List", tags={ "callhistory", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = CallHistoryResponse.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response callHistory(@ApiParam(value = "User phone number and period" ,required=true) CallHistoryRequest callHistoryRequest
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.callHistory(callHistoryRequest,securityContext);
    }
}
