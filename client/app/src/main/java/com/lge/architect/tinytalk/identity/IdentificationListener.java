package com.lge.architect.tinytalk.identity;

public interface IdentificationListener {
  void onComplete(String name, String email, String number, String password);
  void onFailure(String reason);
}
