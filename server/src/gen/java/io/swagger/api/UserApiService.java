package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.NewPasswordInfo;
import io.swagger.model.PhoneNumber;
import io.swagger.model.User;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public abstract class UserApiService {
    public abstract Response changePassword(String xPhoneNumber,String xPassword,NewPasswordInfo newPasswordInfo,SecurityContext securityContext) throws NotFoundException;
    public abstract Response createUser(User user,SecurityContext securityContext) throws NotFoundException;
    public abstract Response deleteUser(String xPhoneNumber,String xPassword,SecurityContext securityContext) throws NotFoundException;
    public abstract Response updateUser(String xPhoneNumber,String xPassword,User user,SecurityContext securityContext) throws NotFoundException;
}
