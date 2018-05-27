package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.TxtMsgHistoryRequest;
import io.swagger.model.TxtMsgHistoryResponse;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-27T15:54:36.606Z")
public abstract class TxtmsghistoryApiService {
    public abstract Response txtMsgHistory(TxtMsgHistoryRequest txtMsgHistoryRequest,SecurityContext securityContext) throws NotFoundException;
}
