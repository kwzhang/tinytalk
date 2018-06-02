package io.swagger.api.factories;

import io.swagger.api.ChangepasswordApiService;
import io.swagger.api.impl.ChangepasswordApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T19:54:34.446Z")
public class ChangepasswordApiServiceFactory {
    private final static ChangepasswordApiService service = new ChangepasswordApiServiceImpl();

    public static ChangepasswordApiService getChangepasswordApi() {
        return service;
    }
}
