package io.swagger.client.api;

import io.swagger.client.ApiException;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DialResponseApi
 */
@Ignore
public class DialResponseApiTest {

    private final DialResponseApi api = new DialResponseApi();

    
    /**
     * Response of call receiver
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void dialResponseTest() throws ApiException {
        String xPhoneNumber = null;
        String xPassword = null;
        String response = null;
        api.dialResponse(xPhoneNumber, xPassword, response);

        // TODO: test validations
    }
    
}
