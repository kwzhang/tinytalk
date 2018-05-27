package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.CallHistoryRequest;
import io.swagger.client.model.CallHistoryResponse;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CallhistoryApi
 */
public class CallhistoryApiTest {

    private final CallhistoryApi api = new CallhistoryApi();

    
    /**
     * Request call history for specified phone number
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void callHistoryTest() throws ApiException {
        CallHistoryRequest callHistoryRequest = null;
        List<CallHistoryResponse> response = api.callHistory(callHistoryRequest);

        // TODO: test validations
    }
    
}
