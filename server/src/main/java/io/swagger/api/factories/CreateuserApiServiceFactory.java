package io.swagger.api.factories;

import io.swagger.api.CreateuserApiService;
import io.swagger.api.impl.CreateuserApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-27T15:54:36.606Z")
public class CreateuserApiServiceFactory {
    private final static CreateuserApiService service = new CreateuserApiServiceImpl();

    public static CreateuserApiService getCreateuserApi() {
        return service;
    }
}
