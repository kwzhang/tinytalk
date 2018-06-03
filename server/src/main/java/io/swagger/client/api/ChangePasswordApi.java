

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


import io.swagger.client.model.NewPasswordInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangePasswordApi {
    private ApiClient apiClient;

    public ChangePasswordApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ChangePasswordApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for changePassword
     * @param xPhoneNumber  (required)
     * @param xPassword  (required)
     * @param newPasswordInfo User phone number to change password (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call changePasswordCall(String xPhoneNumber, String xPassword, NewPasswordInfo newPasswordInfo, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = newPasswordInfo;

        // create path and map variables
        String localVarPath = "/user/password";

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
    private com.squareup.okhttp.Call changePasswordValidateBeforeCall(String xPhoneNumber, String xPassword, NewPasswordInfo newPasswordInfo, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'xPhoneNumber' is set
        if (xPhoneNumber == null) {
            throw new ApiException("Missing the required parameter 'xPhoneNumber' when calling changePassword(Async)");
        }
        
        // verify the required parameter 'xPassword' is set
        if (xPassword == null) {
            throw new ApiException("Missing the required parameter 'xPassword' when calling changePassword(Async)");
        }
        
        // verify the required parameter 'newPasswordInfo' is set
        if (newPasswordInfo == null) {
            throw new ApiException("Missing the required parameter 'newPasswordInfo' when calling changePassword(Async)");
        }
        

        com.squareup.okhttp.Call call = changePasswordCall(xPhoneNumber, xPassword, newPasswordInfo, progressListener, progressRequestListener);
        return call;

    }

    /**
     * change user password
     * This can only be done by the logged in user.
     * @param xPhoneNumber  (required)
     * @param xPassword  (required)
     * @param newPasswordInfo User phone number to change password (required)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public void changePassword(String xPhoneNumber, String xPassword, NewPasswordInfo newPasswordInfo) throws ApiException {
        changePasswordWithHttpInfo(xPhoneNumber, xPassword, newPasswordInfo);
    }

    /**
     * change user password
     * This can only be done by the logged in user.
     * @param xPhoneNumber  (required)
     * @param xPassword  (required)
     * @param newPasswordInfo User phone number to change password (required)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Void> changePasswordWithHttpInfo(String xPhoneNumber, String xPassword, NewPasswordInfo newPasswordInfo) throws ApiException {
        com.squareup.okhttp.Call call = changePasswordValidateBeforeCall(xPhoneNumber, xPassword, newPasswordInfo, null, null);
        return apiClient.execute(call);
    }

    /**
     * change user password (asynchronously)
     * This can only be done by the logged in user.
     * @param xPhoneNumber  (required)
     * @param xPassword  (required)
     * @param newPasswordInfo User phone number to change password (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call changePasswordAsync(String xPhoneNumber, String xPassword, NewPasswordInfo newPasswordInfo, final ApiCallback<Void> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = changePasswordValidateBeforeCall(xPhoneNumber, xPassword, newPasswordInfo, progressListener, progressRequestListener);
        apiClient.executeAsync(call, callback);
        return call;
    }
}
