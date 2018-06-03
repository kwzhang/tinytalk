package io.swagger.api.factories;

import io.swagger.api.DialresponseApiService;
import io.swagger.api.impl.DialresponseApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class DialresponseApiServiceFactory {
    private final static DialresponseApiService service = new DialresponseApiServiceImpl();

    public static DialresponseApiService getDialresponseApi() {
        return service;
    }
}
