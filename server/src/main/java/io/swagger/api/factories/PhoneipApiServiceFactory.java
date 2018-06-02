package io.swagger.api.factories;

import io.swagger.api.PhoneipApiService;
import io.swagger.api.impl.PhoneipApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-02T19:54:34.446Z")
public class PhoneipApiServiceFactory {
    private final static PhoneipApiService service = new PhoneipApiServiceImpl();

    public static PhoneipApiService getPhoneipApi() {
        return service;
    }
}
