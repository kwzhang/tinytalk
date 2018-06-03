package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.PhoneNumber;
import io.swagger.client.model.User;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CreateUserApi
 */
@Ignore
public class CreateUserApiTest {

    private final CreateUserApi api = new CreateUserApi();

    
    /**
     * Create user
     *
     * This can only be done by the logged in user.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createUserTest() throws ApiException {
        User user = null;
        PhoneNumber response = api.createUser(user);

        // TODO: test validations
    }
    
}
