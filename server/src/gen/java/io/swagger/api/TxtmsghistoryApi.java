package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.TxtmsghistoryApiService;
import io.swagger.api.factories.TxtmsghistoryApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.TxtMsgHistoryRequest;
import io.swagger.model.TxtMsgHistoryResponse;

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

@Path("/txtmsghistory")


@io.swagger.annotations.Api(description = "the txtmsghistory API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-27T15:54:36.606Z")
public class TxtmsghistoryApi  {
   private final TxtmsghistoryApiService delegate;

   public TxtmsghistoryApi(@Context ServletConfig servletContext) {
      TxtmsghistoryApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("TxtmsghistoryApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (TxtmsghistoryApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = TxtmsghistoryApiServiceFactory.getTxtmsghistoryApi();
      }

      this.delegate = delegate;
   }

    @POST
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Request text msg history for specified phone number", notes = "", response = TxtMsgHistoryResponse.class, tags={ "txtmsghistory", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = TxtMsgHistoryResponse.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response txtMsgHistory(@ApiParam(value = "User phone number and period" ,required=true) TxtMsgHistoryRequest txtMsgHistoryRequest
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.txtMsgHistory(txtMsgHistoryRequest,securityContext);
    }
}
