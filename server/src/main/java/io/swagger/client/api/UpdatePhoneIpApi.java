

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


import io.swagger.client.model.UpdatePhoneIp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdatePhoneIpApi {
    private ApiClient apiClient;

    public UpdatePhoneIpApi() {
        this(Configuration.getDefaultApiClient());
    }

    public UpdatePhoneIpApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for updatePhoneIp
     * @param xPhoneNumber  (required)
     * @param xPassword  (required)
     * @param updatePhoneIp User phone number and new ip (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updatePhoneIpCall(String xPhoneNumber, String xPassword, UpdatePhoneIp updatePhoneIp, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = updatePhoneIp;

        // create path and map variables
        String localVarPath = "/phoneip";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        if (xPhoneNumber != null)
        localVarHeaderParams.put("x-phone-number", apiClient.parameterToString(xPhoneNumber));
        if (xPassword != null)
        localVarHeaderParams.put("x-password", apiClient.parameterToString(xPassword));

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
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call updatePhoneIpValidateBeforeCall(String xPhoneNumber, String xPassword, UpdatePhoneIp updatePhoneIp, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'xPhoneNumber' is set
        if (xPhoneNumber == null) {
            throw new ApiException("Missing the required parameter 'xPhoneNumber' when calling updatePhoneIp(Async)");
        }
        
        // verify the required parameter 'xPassword' is set
        if (xPassword == null) {
            throw new ApiException("Missing the required parameter 'xPassword' when calling updatePhoneIp(Async)");
        }
        
        // verify the required parameter 'updatePhoneIp' is set
        if (updatePhoneIp == null) {
            throw new ApiException("Missing the required parameter 'updatePhoneIp' when calling updatePhoneIp(Async)");
        }
        

        com.squareup.okhttp.Call call = updatePhoneIpCall(xPhoneNumber, xPassword, updatePhoneIp, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Update phoneip for specified phone number
     * 
     * @param xPhoneNumber  (required)
     * @param xPassword  (required)
     * @param updatePhoneIp User phone number and new ip (required)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public void updatePhoneIp(String xPhoneNumber, String xPassword, UpdatePhoneIp updatePhoneIp) throws ApiException {
        updatePhoneIpWithHttpInfo(xPhoneNumber, xPassword, updatePhoneIp);
    }

    /**
     * Update phoneip for specified phone number
     * 
     * @param xPhoneNumber  (required)
     * @param xPassword  (required)
     * @param updatePhoneIp User phone number and new ip (required)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Void> updatePhoneIpWithHttpInfo(String xPhoneNumber, String xPassword, UpdatePhoneIp updatePhoneIp) throws ApiException {
        com.squareup.okhttp.Call call = updatePhoneIpValidateBeforeCall(xPhoneNumber, xPassword, updatePhoneIp, null, null);
        return apiClient.execute(call);
    }

    /**
     * Update phoneip for specified phone number (asynchronously)
     * 
     * @param xPhoneNumber  (required)
     * @param xPassword  (required)
     * @param updatePhoneIp User phone number and new ip (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updatePhoneIpAsync(String xPhoneNumber, String xPassword, UpdatePhoneIp updatePhoneIp, final ApiCallback<Void> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updatePhoneIpValidateBeforeCall(xPhoneNumber, xPassword, updatePhoneIp, progressListener, progressRequestListener);
        apiClient.executeAsync(call, callback);
        return call;
    }
}
