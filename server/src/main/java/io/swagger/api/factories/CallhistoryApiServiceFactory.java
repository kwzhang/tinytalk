package io.swagger.api.factories;

import io.swagger.api.CallhistoryApiService;
import io.swagger.api.impl.CallhistoryApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-27T15:54:36.606Z")
public class CallhistoryApiServiceFactory {
    private final static CallhistoryApiService service = new CallhistoryApiServiceImpl();

    public static CallhistoryApiService getCallhistoryApi() {
        return service;
    }
}
