package io.swagger.api.factories;

import io.swagger.api.UpdateuserApiService;
import io.swagger.api.impl.UpdateuserApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-27T15:54:36.606Z")
public class UpdateuserApiServiceFactory {
    private final static UpdateuserApiService service = new UpdateuserApiServiceImpl();

    public static UpdateuserApiService getUpdateuserApi() {
        return service;
    }
}
