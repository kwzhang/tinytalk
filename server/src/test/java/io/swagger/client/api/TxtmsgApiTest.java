package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.TxtMsgRequest;
import io.swagger.client.model.TxtMsgResult;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for TxtmsgApi
 */
@Ignore
public class TxtmsgApiTest {

    private final TxtmsgApi api = new TxtmsgApi();

    
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
        TxtMsgRequest body = null;
        TxtMsgResult response = api.txtMsg(body);

        // TODO: test validations
    }
    
}
