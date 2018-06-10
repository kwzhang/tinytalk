package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.PhoneIp;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-10T15:50:21.471Z")
public abstract class DialresponseApiService {
    public abstract Response dialResponse(String xPhoneNumber,String xPassword,String response,PhoneIp phoneIp,SecurityContext securityContext) throws NotFoundException;
}
