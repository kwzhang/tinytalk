package io.swagger.api.factories;

import io.swagger.api.CcrequestApiService;
import io.swagger.api.impl.CcrequestApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T15:11:52.437Z")
public class CcrequestApiServiceFactory {
    private final static CcrequestApiService service = new CcrequestApiServiceImpl();

    public static CcrequestApiService getCcrequestApi() {
        return service;
    }
}
