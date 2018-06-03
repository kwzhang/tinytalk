package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.BillInformation;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for BillApi
 */
@Ignore
public class BillApiTest {

    private final BillApi api = new BillApi();

    
    /**
     * Request billing information
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void billTest() throws ApiException {
        String xPhoneNumber = null;
        String xPassword = null;
        String period = null;
        BillInformation response = api.bill(xPhoneNumber, xPassword, period);

        // TODO: test validations
    }
    
}
