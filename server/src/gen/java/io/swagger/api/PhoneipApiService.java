package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.PhoneIp;
import io.swagger.model.UpdatePhoneIp;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T19:54:34.446Z")
public abstract class PhoneipApiService {
    public abstract Response phoneip(String phonenumber,SecurityContext securityContext) throws NotFoundException;
    public abstract Response updatephoneip(UpdatePhoneIp updatePhoneIp,SecurityContext securityContext) throws NotFoundException;
}
