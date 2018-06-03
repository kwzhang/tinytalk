package io.swagger.client.api;

import io.swagger.client.ApiException;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CallDropApi
 */
@Ignore
public class CallDropApiTest {

    private final CallDropApi api = new CallDropApi();

    
    /**
     * Drop call
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void callDropTest() throws ApiException {
        String xPhoneNumber = null;
        String xPassword = null;
        api.callDrop(xPhoneNumber, xPassword);

        // TODO: test validations
    }
    
}
