package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.TxtMsgHistoryRequest;
import io.swagger.client.model.TxtMsgHistoryResponse;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for TxtmsghistoryApi
 */
@Ignore
public class TxtmsghistoryApiTest {

    private final TxtmsghistoryApi api = new TxtmsghistoryApi();

    
    /**
     * Request text msg history for specified phone number
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void txtMsgHistoryTest() throws ApiException {
        TxtMsgHistoryRequest txtMsgHistoryRequest = null;
        TxtMsgHistoryResponse response = api.txtMsgHistory(txtMsgHistoryRequest);

        // TODO: test validations
    }
    
}
