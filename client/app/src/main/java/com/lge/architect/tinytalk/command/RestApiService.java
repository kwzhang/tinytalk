package com.lge.architect.tinytalk.command;

import com.lge.architect.tinytalk.command.model.Dial;
import com.lge.architect.tinytalk.command.model.DialResponse;
import com.lge.architect.tinytalk.command.model.RegisterResult;
import com.lge.architect.tinytalk.command.model.TextMessage;
import com.lge.architect.tinytalk.command.model.User;
import com.lge.architect.tinytalk.command.model.UserLogin;
import com.lge.architect.tinytalk.command.model.UserPassword;
import com.lge.architect.tinytalk.command.model.UserResetPassword;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestApiService {
  @POST(User.URI)
  Call<RegisterResult> registerUser(@HeaderMap() Map<String, String> headers, @Body User user);

  @PUT(User.URI)
  Call<RegisterResult> updateUser(@HeaderMap() Map<String, String> headers, @Body User user);

  @GET(User.URI)
  Call<User> getUser(@HeaderMap() Map<String, String> headers);

  @DELETE(User.URI)
  Call<Void> deleteUser();

  @POST(UserLogin.URI)
  Call<UserLogin> login(@HeaderMap Map<String, String> headers);

  @POST(UserPassword.URI)
  Call<Void> changePassword(@HeaderMap() Map<String, String> headers, @Body UserPassword userPassword);

  @POST(UserResetPassword.URI)
  Call<UserPassword> resetPassword(@HeaderMap() Map<String, String> headers, @Body UserResetPassword userPassword);

  @POST(Dial.URI)
  Call<Void> dial(@HeaderMap() Map<String, String> headers, @Body Dial dial);

  @DELETE(Dial.URI)
  Call<Void> dropCall(@HeaderMap() Map<String, String> headers);

  @POST(DialResponse.URI)
  Call<Void> dialResponse(@Path("type") DialResponse.Type type, @HeaderMap() Map<String, String> headers, @Body DialResponse dialResponse);

  @POST(TextMessage.URI)
  Call<Void> sendTextMessage(@HeaderMap() Map<String, String> headers, @Body TextMessage textMessage);
}
