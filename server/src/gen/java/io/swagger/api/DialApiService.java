package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.DialRequest;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public abstract class DialApiService {
    public abstract Response callDial(String xPhoneNumber,String xPassword,DialRequest dialRequest,SecurityContext securityContext) throws NotFoundException;
    public abstract Response callDrop(String xPhoneNumber,String xPassword,SecurityContext securityContext) throws NotFoundException;
}
