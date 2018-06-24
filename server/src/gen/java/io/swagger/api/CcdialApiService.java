package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.InlineResponse200;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public abstract class CcdialApiService {
    public abstract Response callCcDial(String xPhoneNumber,String xPassword,String ccnumber,Ip ip,SecurityContext securityContext) throws NotFoundException;
    public abstract Response dropCcDial(String xPhoneNumber,String xPassword,String ccnumber,SecurityContext securityContext) throws NotFoundException;
}
