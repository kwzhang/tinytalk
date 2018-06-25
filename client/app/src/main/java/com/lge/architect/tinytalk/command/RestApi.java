package com.lge.architect.tinytalk.command;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.lge.architect.tinytalk.command.model.Dial;
import com.lge.architect.tinytalk.command.model.DialResponse;
import com.lge.architect.tinytalk.command.model.RegisterResult;
import com.lge.architect.tinytalk.command.model.TextMessage;
import com.lge.architect.tinytalk.command.model.User;
import com.lge.architect.tinytalk.command.model.UserLogin;
import com.lge.architect.tinytalk.command.model.UserPassword;
import com.lge.architect.tinytalk.command.model.CreditCard;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.identity.IdentificationListener;
import com.lge.architect.tinytalk.identity.Identity;
import com.lge.architect.tinytalk.identity.UserInfoListener;
import com.lge.architect.tinytalk.voicecall.CallSessionService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApi {
  public static final String AMAZON_HOST = "35.168.51.250";
  public static final String LOCAL_HOST = "10.0.1.171";

  public static final String HOST_IP_ADDRESS = LOCAL_HOST;

  private static final String TAG = RestApi.class.getSimpleName();
  private static final String HTTP_SERVER_URI = "http://" + HOST_IP_ADDRESS + ":8080/designcraft/SWArchi2018_3/designcraft/1.0.0/";

  private static RestApi instance = null;
  private RestApiService service;

  private static final String HEADER_PHONE_NUMBER = "x-phone-number";
  private static final String HEADER_PASSWORD = "x-password";

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

  private static Map<String, String> getDefaultHeaders(Identity identity) {
    Map<String, String> headers = new HashMap<>();

    headers.put("Content-Type", "application/json");
    headers.put(HEADER_PHONE_NUMBER, identity.getNumber());
    headers.put(HEADER_PASSWORD, identity.getPassword());

    return headers;
  }

  private static Map<String, String> getEmptyHeaders() {
    Map<String, String> headers = new HashMap<>();

    headers.put("Content-Type", "application/json");

    return headers;
  }

  private static Map<String, String> getHeaders(Context context) {
    return getDefaultHeaders(Identity.getInstance(context.getApplicationContext()));
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

  public void callDial(Context context, Contact receiver, String localAddress) {
    Call<Void> call = service.dial(getHeaders(context), new Dial(receiver.getPhoneNumber(), localAddress));

    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
        CallSessionService.enqueueWork(context, new Intent(CallSessionService.ACTION_OUTGOING_CALL)
                .putExtra(CallSessionService.EXTRA_NAME_OR_NUMBER, receiver.toString()));
      }

      @Override
      public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
      }
    });
  }

  public void acceptCall(Context context, String remoteAddress) {
    Call<Void> call = service.dialResponse(DialResponse.Type.ACCEPT, getHeaders(context), new DialResponse());

    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
        CallSessionService.enqueueWork(context, new Intent(CallSessionService.ACTION_ANSWER_CALL)
                .putExtra(CallSessionService.EXTRA_REMOTE_HOST_URI, remoteAddress)
        );
      }

      @Override
      public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
      }
    });
  }

  public void denyCall(Context context) {
    Call<Void> call = service.dialResponse(DialResponse.Type.DENY, getHeaders(context), new DialResponse());

    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
      }

      @Override
      public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
      }
    });
  }

  public void hangup(Context context) {
    Call<Void> call = service.dropCall(getHeaders(context));

    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
      }

      @Override
      public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
      }
    });

    CallSessionService.enqueueWork(context, new Intent(CallSessionService.ACTION_LOCAL_HANGUP));
  }

  public void register(User user, IdentificationListener listener) {
    Call<RegisterResult> call = service.registerUser(getEmptyHeaders(), user);

    call.enqueue(new Callback<RegisterResult>() {
      @Override
      public void onResponse(@NonNull Call<RegisterResult> call, @NonNull Response<RegisterResult> response) {
        RegisterResult result = response.body();

        if (result != null) {
          listener.onComplete(user.getName(), user.getEmail(), result.getNumber(), user.getPassword());
        }
      }

      @Override
      public void onFailure(@NonNull Call<RegisterResult> call, @NonNull Throwable t) {
        listener.onFailure(t.getMessage());
      }
    });
  }

  public void login(String number, String password, IdentificationListener listener) {
    Map<String, String> headers = getEmptyHeaders();
    headers.put(HEADER_PHONE_NUMBER, number);
    headers.put(HEADER_PASSWORD, password);

    Call<UserLogin> call = service.login(headers);

    call.enqueue(new Callback<UserLogin>() {
      @Override
      public void onResponse(@NonNull Call<UserLogin> call, @NonNull Response<UserLogin> response) {
        UserLogin result = response.body();

        if (result != null) {
          listener.onComplete(result.getName(), result.getEmail(), number, password);
        } else {
          listener.onFailure("Empty body");
        }
      }

      @Override
      public void onFailure(@NonNull Call<UserLogin> call, @NonNull Throwable t) {
        listener.onFailure(t.getMessage());
      }
    });
  }

  public void updateUser(String number, String oldPassword, User user, IdentificationListener listener) {
    Map<String, String> headers = getEmptyHeaders();
    headers.put(HEADER_PHONE_NUMBER, number);
    headers.put(HEADER_PASSWORD, oldPassword);

    Call<RegisterResult> call = service.updateUser(headers, user);

    call.enqueue(new Callback<RegisterResult>() {
      @Override
      public void onResponse(@NonNull Call<RegisterResult> call, @NonNull Response<RegisterResult> response) {
        listener.onComplete(user.getName(), user.getEmail(), number, user.getPassword());
      }

      @Override
      public void onFailure(@NonNull Call<RegisterResult> call, @NonNull Throwable t) {
        listener.onFailure(t.getMessage());
      }
    });
  }

  public void getUser(Context context, UserInfoListener listener) {
    Call<User> call = service.getUser(getHeaders(context));

    call.enqueue(new Callback<User>() {
      @Override
      public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
        User user = response.body();

        if (user != null) {
          listener.onResponse(user);
        } else {
          listener.onFailure("Empty user body");
        }
      }

      @Override
      public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
        listener.onFailure(t.getMessage());
      }
    });
  }

  public void resetPassword(String phoneNumber, String cardNumber, String expiryDate, String cvvCode,
                            IdentificationListener listener) {
    Map<String, String> headers = getEmptyHeaders();
    headers.put(HEADER_PHONE_NUMBER, phoneNumber);

    Call<UserPassword> call = service.resetPassword(headers,
        new CreditCard(cardNumber, expiryDate, cvvCode));

    call.enqueue(new Callback<UserPassword>() {
      @Override
      public void onResponse(@NonNull Call<UserPassword> call, @NonNull Response<UserPassword> response) {
        UserPassword userPassword = response.body();

        if (userPassword != null) {
          listener.onComplete("", "", phoneNumber, userPassword.getPassword());
        } else {
          listener.onFailure("Empty password body");
        }
      }

      @Override
      public void onFailure(@NonNull Call<UserPassword> call, @NonNull Throwable t) {
        listener.onFailure(t.getMessage());
      }
    });
  }

  public void changePassword(String phoneNumber, String oldPassword, String newPassword,
                            IdentificationListener listener) {
    Map<String, String> headers = getEmptyHeaders();
    headers.put(HEADER_PHONE_NUMBER, phoneNumber);
    headers.put(HEADER_PASSWORD, oldPassword);

    Call<Void> call = service.changePassword(headers,
        new UserPassword(oldPassword, newPassword));

    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
        listener.onComplete("", "", phoneNumber, newPassword);
      }

      @Override
      public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
        listener.onFailure(t.getMessage());
      }
    });
  }
}
