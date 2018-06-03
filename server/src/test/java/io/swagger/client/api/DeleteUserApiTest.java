package io.swagger.client.api;

import io.swagger.client.ApiException;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DeleteUserApi
 */
@Ignore
public class DeleteUserApiTest {

    private final DeleteUserApi api = new DeleteUserApi();

    
    /**
     * Delete user for specified phone number
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteUserTest() throws ApiException {
        String xPhoneNumber = null;
        String xPassword = null;
        api.deleteUser(xPhoneNumber, xPassword);

        // TODO: test validations
    }
    
}
