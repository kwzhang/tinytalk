package io.swagger.api.impl;

import io.swagger.api.*;
import io.swagger.model.*;

import io.swagger.model.TxtMsgRequest;
import io.swagger.model.TxtMsgResult;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-27T15:54:36.606Z")
public class TxtmsgApiServiceImpl extends TxtmsgApiService {
    @Override
    public Response txtMsg(TxtMsgRequest body, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
    	System.out.println(body.toString());
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}