package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.DialRequest;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DialApi
 */
@Ignore
public class DialApiTest {

    private final DialApi api = new DialApi();

    
    /**
     * Dial voice call
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void callDialTest() throws ApiException {
        String xPhoneNumber = null;
        String xPassword = null;
        DialRequest dialRequest = null;
        api.callDial(xPhoneNumber, xPassword, dialRequest);

        // TODO: test validations
    }
    
}
