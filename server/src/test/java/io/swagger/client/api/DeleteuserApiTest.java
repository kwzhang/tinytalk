package io.swagger.client.api;

import io.swagger.client.ApiException;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DeleteuserApi
 */
@Ignore
public class DeleteuserApiTest {

    private final DeleteuserApi api = new DeleteuserApi();

    
    /**
     * Delete user for specified phone number
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteuserTest() throws ApiException {
        String phonenumber = null;
        api.deleteuser(phonenumber);

        // TODO: test validations
    }
    
}
