package com.lge.architect.tinytalk.command;

import com.lge.architect.tinytalk.command.model.Billing;
import com.lge.architect.tinytalk.command.model.ConferenceCall;
import com.lge.architect.tinytalk.command.model.ConferenceCallResult;
import com.lge.architect.tinytalk.command.model.ConferenceCallSchedule;
import com.lge.architect.tinytalk.command.model.CreditCard;
import com.lge.architect.tinytalk.command.model.Dial;
import com.lge.architect.tinytalk.command.model.DialResponse;
import com.lge.architect.tinytalk.command.model.RegisterResult;
import com.lge.architect.tinytalk.command.model.TextMessage;
import com.lge.architect.tinytalk.command.model.User;
import com.lge.architect.tinytalk.command.model.UserLogin;
import com.lge.architect.tinytalk.command.model.UserPassword;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

  @PUT(UserPassword.URI)
  Call<Void> changePassword(@HeaderMap() Map<String, String> headers, @Body UserPassword userPassword);

  @PUT(CreditCard.URI)
  Call<UserPassword> resetPassword(@HeaderMap() Map<String, String> headers, @Body CreditCard userPassword);

  @POST(Dial.URI)
  Call<Void> dial(@HeaderMap() Map<String, String> headers, @Body Dial dial);

  @DELETE(Dial.URI)
  Call<Void> dropCall(@HeaderMap() Map<String, String> headers);

  @POST(DialResponse.URI)
  Call<Void> dialResponse(@Path("type") DialResponse.Type type, @HeaderMap() Map<String, String> headers, @Body DialResponse dialResponse);

  @POST(TextMessage.URI)
  Call<Void> sendTextMessage(@HeaderMap() Map<String, String> headers, @Body TextMessage textMessage);

  @GET(Billing.URI)
  Call<Billing> getBilling(@HeaderMap() Map<String, String> headers, @Query(Billing.PERIOD) String period);

  @POST(ConferenceCallSchedule.URI)
  Call<Void> scheduleConferenceCall(@HeaderMap Map<String, String> headers, @Body ConferenceCallSchedule schedule);

  @POST(ConferenceCall.URI)
  Call<ConferenceCallResult> startConferenceCall(@HeaderMap Map<String, String> headers, @Path(ConferenceCall.MEMBERS) String members, @Body ConferenceCall conferenceCall);

  @DELETE(ConferenceCall.URI)
  Call<Void> endConferenceCall(@HeaderMap Map<String, String> headers, @Path(ConferenceCall.MEMBERS) String members);
}
