

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


import io.swagger.client.model.CallHistoryRequest;
import io.swagger.client.model.CallHistoryResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallhistoryApi {
    private ApiClient apiClient;

    public CallhistoryApi() {
        this(Configuration.getDefaultApiClient());
    }

    public CallhistoryApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for callHistory
     * @param callHistoryRequest User phone number and period (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call callHistoryCall(CallHistoryRequest callHistoryRequest, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = callHistoryRequest;

        // create path and map variables
        String localVarPath = "/callhistory";

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
    private com.squareup.okhttp.Call callHistoryValidateBeforeCall(CallHistoryRequest callHistoryRequest, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'callHistoryRequest' is set
        if (callHistoryRequest == null) {
            throw new ApiException("Missing the required parameter 'callHistoryRequest' when calling callHistory(Async)");
        }
        

        com.squareup.okhttp.Call call = callHistoryCall(callHistoryRequest, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Request call history for specified phone number
     * 
     * @param callHistoryRequest User phone number and period (required)
     * @return List&lt;CallHistoryResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<CallHistoryResponse> callHistory(CallHistoryRequest callHistoryRequest) throws ApiException {
        ApiResponse<List<CallHistoryResponse>> resp = callHistoryWithHttpInfo(callHistoryRequest);
        return resp.getData();
    }

    /**
     * Request call history for specified phone number
     * 
     * @param callHistoryRequest User phone number and period (required)
     * @return ApiResponse&lt;List&lt;CallHistoryResponse&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<CallHistoryResponse>> callHistoryWithHttpInfo(CallHistoryRequest callHistoryRequest) throws ApiException {
        com.squareup.okhttp.Call call = callHistoryValidateBeforeCall(callHistoryRequest, null, null);
        Type localVarReturnType = new TypeToken<List<CallHistoryResponse>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Request call history for specified phone number (asynchronously)
     * 
     * @param callHistoryRequest User phone number and period (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call callHistoryAsync(CallHistoryRequest callHistoryRequest, final ApiCallback<List<CallHistoryResponse>> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = callHistoryValidateBeforeCall(callHistoryRequest, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<CallHistoryResponse>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
