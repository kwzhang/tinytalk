

package io.swagger.client.api;

import io.swagger.client.ApiCallback;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;
import io.swagger.client.ProgressRequestBody;
import io.swagger.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import io.swagger.client.model.TxtMsgHistoryRequest;
import io.swagger.client.model.TxtMsgHistoryResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TxtmsghistoryApi {
    private ApiClient apiClient;

    public TxtmsghistoryApi() {
        this(Configuration.getDefaultApiClient());
    }

    public TxtmsghistoryApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for txtMsgHistory
     * @param txtMsgHistoryRequest User phone number and period (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call txtMsgHistoryCall(TxtMsgHistoryRequest txtMsgHistoryRequest, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = txtMsgHistoryRequest;

        // create path and map variables
        String localVarPath = "/txtmsghistory";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call txtMsgHistoryValidateBeforeCall(TxtMsgHistoryRequest txtMsgHistoryRequest, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'txtMsgHistoryRequest' is set
        if (txtMsgHistoryRequest == null) {
            throw new ApiException("Missing the required parameter 'txtMsgHistoryRequest' when calling txtMsgHistory(Async)");
        }
        

        com.squareup.okhttp.Call call = txtMsgHistoryCall(txtMsgHistoryRequest, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Request text msg history for specified phone number
     * 
     * @param txtMsgHistoryRequest User phone number and period (required)
     * @return TxtMsgHistoryResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TxtMsgHistoryResponse txtMsgHistory(TxtMsgHistoryRequest txtMsgHistoryRequest) throws ApiException {
        ApiResponse<TxtMsgHistoryResponse> resp = txtMsgHistoryWithHttpInfo(txtMsgHistoryRequest);
        return resp.getData();
    }

    /**
     * Request text msg history for specified phone number
     * 
     * @param txtMsgHistoryRequest User phone number and period (required)
     * @return ApiResponse&lt;TxtMsgHistoryResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TxtMsgHistoryResponse> txtMsgHistoryWithHttpInfo(TxtMsgHistoryRequest txtMsgHistoryRequest) throws ApiException {
        com.squareup.okhttp.Call call = txtMsgHistoryValidateBeforeCall(txtMsgHistoryRequest, null, null);
        Type localVarReturnType = new TypeToken<TxtMsgHistoryResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Request text msg history for specified phone number (asynchronously)
     * 
     * @param txtMsgHistoryRequest User phone number and period (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call txtMsgHistoryAsync(TxtMsgHistoryRequest txtMsgHistoryRequest, final ApiCallback<TxtMsgHistoryResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = txtMsgHistoryValidateBeforeCall(txtMsgHistoryRequest, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TxtMsgHistoryResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
