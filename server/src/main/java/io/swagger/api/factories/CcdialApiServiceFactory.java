package io.swagger.api.factories;

import io.swagger.api.CcdialApiService;
import io.swagger.api.impl.CcdialApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class CcdialApiServiceFactory {
    private final static CcdialApiService service = new CcdialApiServiceImpl();

    public static CcdialApiService getCcdialApi() {
        return service;
    }
}
