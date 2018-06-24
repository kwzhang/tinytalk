package com.lge.architect.tinytalk.identity;

import com.lge.architect.tinytalk.command.model.User;

public interface UserInfoListener {
  void onResponse(User user);
  void onFailure(String reason);
}
