package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.InlineResponse2003;
import io.swagger.model.PriceInformation;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-19T19:21:29.273Z")
public abstract class AdminApiService {
    public abstract Response getPrice(String xPhoneNumber,String xPassword,SecurityContext securityContext) throws NotFoundException;
    public abstract Response updatePrice(String xPhoneNumber,String xPassword,PriceInformation priceInformation,SecurityContext securityContext) throws NotFoundException;
}
