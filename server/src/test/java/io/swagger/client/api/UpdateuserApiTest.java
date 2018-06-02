package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.User;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for UpdateuserApi
 */
@Ignore
public class UpdateuserApiTest {

    private final UpdateuserApi api = new UpdateuserApi();

    
    /**
     * Update user
     *
     * This can only be done by the logged in user.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateUserTest() throws ApiException {
        User user = null;
        api.updateUser(user);

        // TODO: test validations
    }
    
}
