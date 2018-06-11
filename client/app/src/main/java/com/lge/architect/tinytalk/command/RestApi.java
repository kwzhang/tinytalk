package com.lge.architect.tinytalk.command;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.lge.architect.tinytalk.command.model.Dial;
import com.lge.architect.tinytalk.command.model.DialResponse;
import com.lge.architect.tinytalk.command.model.TextMessage;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.identity.Identity;
import com.lge.architect.tinytalk.voicecall.VoiceCallService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

  private static Map<String, String> getHeaders(Identity identity) {
    Map<String, String> headers = new HashMap<>();

    headers.put("Content-Type", "application/json");
    headers.put("x-phone-number", identity.getNumber());
    headers.put("x-password", identity.getPassword());

    return headers;
  }

  private static Map<String, String> getHeaders(Context context) {
    return getHeaders(Identity.getInstance(context.getApplicationContext()));
  }

  public void sendTextMessage(Context context, Set<String> receivers, String message) {
    Identity identity = Identity.getInstance(context.getApplicationContext());

    Call<Void> call = service.sendTextMessage(getHeaders(context), new TextMessage(identity.getNumber(), receivers, message));

    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
        Log.d(TAG, "Text message sent: " + response.code());
      }

      @Override
      public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
        Log.e(TAG, "Sending text message failed: " + t.getMessage());
      }
    });
  }

  public void callDial(Context context, Contact receiver) {
    Call<Void> call = service.dial(getHeaders(context), new Dial(receiver.getPhoneNumber()));

    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
        JobIntentService.enqueueWork(context, VoiceCallService.class, VoiceCallService.JOB_ID,
            new Intent(VoiceCallService.ACTION_OUTGOING_CALL).putExtra(VoiceCallService.EXTRA_NAME_OR_NUMBER, receiver.toString()));
      }

      @Override
      public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
      }
    });
  }

  public void acceptDial(Context context) {
    Call<Void> call = service.dialResponse(DialResponse.Type.ACCEPT, getHeaders(context), new DialResponse());

    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
        JobIntentService.enqueueWork(context, VoiceCallService.class, VoiceCallService.JOB_ID,
            new Intent(VoiceCallService.ACTION_ANSWER_CALL));
      }

      @Override
      public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
      }
    });
  }
}
