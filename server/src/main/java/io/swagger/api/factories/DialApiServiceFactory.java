package io.swagger.api.factories;

import io.swagger.api.DialApiService;
import io.swagger.api.impl.DialApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class DialApiServiceFactory {
    private final static DialApiService service = new DialApiServiceImpl();

    public static DialApiService getDialApi() {
        return service;
    }
}
