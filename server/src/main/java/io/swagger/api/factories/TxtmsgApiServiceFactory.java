package io.swagger.api.factories;

import io.swagger.api.TxtmsgApiService;
import io.swagger.api.impl.TxtmsgApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-27T15:54:36.606Z")
public class TxtmsgApiServiceFactory {
    private final static TxtmsgApiService service = new TxtmsgApiServiceImpl();

    public static TxtmsgApiService getTxtmsgApi() {
        return service;
    }
}
