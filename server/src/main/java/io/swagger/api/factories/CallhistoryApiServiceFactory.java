package io.swagger.api.factories;

import io.swagger.api.CallhistoryApiService;
import io.swagger.api.impl.CallhistoryApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T15:11:52.437Z")
public class CallhistoryApiServiceFactory {
    private final static CallhistoryApiService service = new CallhistoryApiServiceImpl();

    public static CallhistoryApiService getCallhistoryApi() {
        return service;
    }
}
