package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.CCRequestInformation;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CcRequestApi
 */
@Ignore
public class CcRequestApiTest {

    private final CcRequestApi api = new CcRequestApi();

    
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
        String xPhoneNumber = null;
        String xPassword = null;
        CCRequestInformation ccrequest = null;
        api.ccRequest(xPhoneNumber, xPassword, ccrequest);

        // TODO: test validations
    }
    
}
