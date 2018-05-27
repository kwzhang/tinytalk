package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.NewPasswordInfo;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ChangepasswordApi
 */
@Ignore
public class ChangepasswordApiTest {

    private final ChangepasswordApi api = new ChangepasswordApi();

    
    /**
     * change user password
     *
     * This can only be done by the logged in user.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void changePasswordTest() throws ApiException {
        NewPasswordInfo newPasswordInfo = null;
        api.changePassword(newPasswordInfo);

        // TODO: test validations
    }
    
}
