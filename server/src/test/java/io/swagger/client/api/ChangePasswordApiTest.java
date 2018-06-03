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
 * API tests for ChangePasswordApi
 */
@Ignore
public class ChangePasswordApiTest {

    private final ChangePasswordApi api = new ChangePasswordApi();

    
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
        String xPhoneNumber = null;
        String xPassword = null;
        NewPasswordInfo newPasswordInfo = null;
        api.changePassword(xPhoneNumber, xPassword, newPasswordInfo);

        // TODO: test validations
    }
    
}
