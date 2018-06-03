

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


import io.swagger.client.model.BillInformation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillApi {
    private ApiClient apiClient;

    public BillApi() {
        this(Configuration.getDefaultApiClient());
    }

    public BillApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for bill
     * @param xPhoneNumber  (required)
     * @param xPassword  (required)
     * @param period  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call billCall(String xPhoneNumber, String xPassword, String period, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/bill";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (period != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("period", period));

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
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call billValidateBeforeCall(String xPhoneNumber, String xPassword, String period, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'xPhoneNumber' is set
        if (xPhoneNumber == null) {
            throw new ApiException("Missing the required parameter 'xPhoneNumber' when calling bill(Async)");
        }
        
        // verify the required parameter 'xPassword' is set
        if (xPassword == null) {
            throw new ApiException("Missing the required parameter 'xPassword' when calling bill(Async)");
        }
        
        // verify the required parameter 'period' is set
        if (period == null) {
            throw new ApiException("Missing the required parameter 'period' when calling bill(Async)");
        }
        

        com.squareup.okhttp.Call call = billCall(xPhoneNumber, xPassword, period, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Request billing information
     * 
     * @param xPhoneNumber  (required)
     * @param xPassword  (required)
     * @param period  (required)
     * @return BillInformation
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public BillInformation bill(String xPhoneNumber, String xPassword, String period) throws ApiException {
        ApiResponse<BillInformation> resp = billWithHttpInfo(xPhoneNumber, xPassword, period);
        return resp.getData();
    }

    /**
     * Request billing information
     * 
     * @param xPhoneNumber  (required)
     * @param xPassword  (required)
     * @param period  (required)
     * @return ApiResponse&lt;BillInformation&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<BillInformation> billWithHttpInfo(String xPhoneNumber, String xPassword, String period) throws ApiException {
        com.squareup.okhttp.Call call = billValidateBeforeCall(xPhoneNumber, xPassword, period, null, null);
        Type localVarReturnType = new TypeToken<BillInformation>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Request billing information (asynchronously)
     * 
     * @param xPhoneNumber  (required)
     * @param xPassword  (required)
     * @param period  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call billAsync(String xPhoneNumber, String xPassword, String period, final ApiCallback<BillInformation> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = billValidateBeforeCall(xPhoneNumber, xPassword, period, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<BillInformation>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
