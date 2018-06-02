package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.CallHistoryRequest;
import io.swagger.model.CallHistoryResponse;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T14:27:00.551Z")
public class CallhistoryApiServiceImpl extends CallhistoryApiService {
    @Override
    public Response callHistory(CallHistoryRequest callHistoryRequest, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(callHistoryRequest.toString());
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
