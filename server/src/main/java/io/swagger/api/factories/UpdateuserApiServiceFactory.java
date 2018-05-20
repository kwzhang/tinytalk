package io.swagger.api.factories;

import io.swagger.api.UpdateuserApiService;
import io.swagger.api.impl.UpdateuserApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-20T01:55:30.397Z")
public class UpdateuserApiServiceFactory {
    private final static UpdateuserApiService service = new UpdateuserApiServiceImpl();

    public static UpdateuserApiService getUpdateuserApi() {
        return service;
    }
}
