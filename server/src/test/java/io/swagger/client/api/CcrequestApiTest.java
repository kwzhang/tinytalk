package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.CCRequest;
import io.swagger.client.model.PhoneNumber;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CcrequestApi
 */
@Ignore
public class CcrequestApiTest {

    private final CcrequestApi api = new CcrequestApi();

    
    /**
     * Send request for conference call
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void ccRequestTest() throws ApiException {
        CCRequest body = null;
        PhoneNumber response = api.ccRequest(body);

        // TODO: test validations
    }
    
}
