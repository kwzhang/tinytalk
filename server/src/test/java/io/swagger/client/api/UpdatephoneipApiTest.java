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
 * API tests for UpdatephoneipApi
 */
@Ignore
public class UpdatephoneipApiTest {

    private final UpdatephoneipApi api = new UpdatephoneipApi();

    
    /**
     * Update phoneip for specified phone number
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updatephoneipTest() throws ApiException {
        UpdatePhoneIp updatePhoneIp = null;
        api.updatephoneip(updatePhoneIp);

        // TODO: test validations
    }
    
}
