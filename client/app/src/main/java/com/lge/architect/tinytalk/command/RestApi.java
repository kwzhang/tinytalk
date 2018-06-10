package com.lge.architect.tinytalk.command;

import android.util.Log;

import com.lge.architect.tinytalk.command.model.TextMessage;
import com.lge.architect.tinytalk.identity.Identity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApi {
  private static final String TAG = RestApi.class.getSimpleName();
  private static final String HTTP_SERVER_URI = "http://18.232.140.183:8080/designcraft/SWArchi2018_3/designcraft/1.0.0/";

  private static RestApi instance = null;
  private RestApiService service = null;

  protected RestApi() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(HTTP_SERVER_URI)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    service = retrofit.create(RestApiService.class);
  }

  public synchronized static RestApi getInstance() {
    if (instance == null) {
      instance = new RestApi();
    }

    return instance;
  }

  public void sendTextMessage(Identity identity, List<String> receivers, String message) throws IOException {
    Map<String, String> headers = new HashMap<>();

    headers.put("Content-Type", "application/json");
    headers.put("x-phone-number", identity.getNumber());
    headers.put("x-password", identity.getPassword());

    Call<Void> call = service.sendTextMessage(headers, new TextMessage(receivers, message));

    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        Log.d(TAG, "Text message sent: " + response.code());
      }

      @Override
      public void onFailure(Call<Void> call, Throwable t) {
        Log.e(TAG, "Sending text message failed: " + t.getMessage());
      }
    });
  }
}
