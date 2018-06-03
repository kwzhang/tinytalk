package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.UpdatePhoneIp;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for UpdatePhoneIpApi
 */
@Ignore
public class UpdatePhoneIpApiTest {

    private final UpdatePhoneIpApi api = new UpdatePhoneIpApi();

    
    /**
     * Update phoneip for specified phone number
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updatePhoneIpTest() throws ApiException {
        String xPhoneNumber = null;
        String xPassword = null;
        UpdatePhoneIp updatePhoneIp = null;
        api.updatePhoneIp(xPhoneNumber, xPassword, updatePhoneIp);

        // TODO: test validations
    }
    
}
