package io.swagger.client.api;

import io.swagger.client.ApiException;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DropCcDialApi
 */
@Ignore
public class DropCcDialApiTest {

    private final DropCcDialApi api = new DropCcDialApi();

    
    /**
     * Dial to conference call number
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void dropCcDialTest() throws ApiException {
        String xPhoneNumber = null;
        String xPassword = null;
        String ccnumber = null;
        api.dropCcDial(xPhoneNumber, xPassword, ccnumber);

        // TODO: test validations
    }
    
}
