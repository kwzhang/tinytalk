package com.lge.architect.tinytalk.command;

import com.lge.architect.tinytalk.command.model.Dial;
import com.lge.architect.tinytalk.command.model.DialResponse;
import com.lge.architect.tinytalk.command.model.RegisterResult;
import com.lge.architect.tinytalk.command.model.TextMessage;
import com.lge.architect.tinytalk.command.model.User;
import com.lge.architect.tinytalk.command.model.UserPassword;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestApiService {
  @POST(User.URI)
  Call<RegisterResult> registerUser(@HeaderMap() Map<String, String> headers, @Body User user);

  @PUT(User.URI)
  Call<Void> updateUser(@HeaderMap() Map<String, String> headers, @Body User user);

  @DELETE(User.URI)
  Call<Void> deleteUser();

  @POST(UserPassword.URI)
  Call<Void> changePassword(@HeaderMap() Map<String, String> headers, @Body UserPassword userPassword);

  @POST(Dial.URI)
  Call<Void> dial(@HeaderMap() Map<String, String> headers, @Body Dial dial);

  @DELETE(Dial.URI)
  Call<Void> dropCall(@HeaderMap() Map<String, String> headers);

  @POST(DialResponse.URI)
  Call<Void> dialResponse(@Path("type") DialResponse.Type type, @HeaderMap() Map<String, String> headers, @Body DialResponse dialResponse);

  @POST(TextMessage.URI)
  Call<Void> sendTextMessage(@HeaderMap() Map<String, String> headers, @Body TextMessage textMessage);
}
