package com.lge.architect.tinytalk.command;

import com.lge.architect.tinytalk.command.model.TextMessage;
import com.lge.architect.tinytalk.database.model.Contact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApi {
  private static final String HTTP_SERVER_URI = "http://18.232.140.183:8080/designcraft/SWArchi2018_3/designcraft/1.0.0/";

  private static RestApi instance = null;
  private RestApiService service = null;
  private static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Accept", "application/json");
    headers.put("x-phone-number", "");
    headers.put("x-password", "");
  }

  protected RestApi() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(HTTP_SERVER_URI)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    service = retrofit.create(RestApiService.class);
  }

  public synchronized static RestApi getInstance() {
    if (instance != null) {
      instance = new RestApi();
    }

    return instance;
  }

  public void sendTextMessage(List<Contact> contacts, String message) {
    List<String> numbers = contacts.stream().map(Contact::getPhoneNumber).collect(Collectors.toCollection(() -> new ArrayList<>(contacts.size())));

    Call<Void> call = service.sendTextMessage(headers, new TextMessage(numbers, message));
    try {
      call.execute();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
