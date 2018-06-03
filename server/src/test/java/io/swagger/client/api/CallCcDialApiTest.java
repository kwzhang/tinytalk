package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.InlineResponse200;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CallCcDialApi
 */
@Ignore
public class CallCcDialApiTest {

    private final CallCcDialApi api = new CallCcDialApi();

    
    /**
     * Dial to conference call number
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void callCcDialTest() throws ApiException {
        String xPhoneNumber = null;
        String xPassword = null;
        String ccnumber = null;
        InlineResponse200 response = api.callCcDial(xPhoneNumber, xPassword, ccnumber);

        // TODO: test validations
    }
    
}
