package com.lge.architect.tinytalk.registration;

public interface RegistrationResultListener {
  void onComplete(String name, String number, String password);
  void onFailure(String reason);
}
