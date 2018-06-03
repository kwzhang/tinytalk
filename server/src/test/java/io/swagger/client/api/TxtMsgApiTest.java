package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.TxtMsgRequest;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for TxtMsgApi
 */
@Ignore
public class TxtMsgApiTest {

    private final TxtMsgApi api = new TxtMsgApi();

    
    /**
     * Send text message
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void txtMsgTest() throws ApiException {
        String xPhoneNumber = null;
        String xPassword = null;
        TxtMsgRequest body = null;
        api.txtMsg(xPhoneNumber, xPassword, body);

        // TODO: test validations
    }
    
}
