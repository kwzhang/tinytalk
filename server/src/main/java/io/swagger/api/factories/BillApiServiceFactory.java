package io.swagger.api.factories;

import io.swagger.api.BillApiService;
import io.swagger.api.impl.BillApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-06-03T22:43:20.301Z")
public class BillApiServiceFactory {
    private final static BillApiService service = new BillApiServiceImpl();

    public static BillApiService getBillApi() {
        return service;
    }
}
