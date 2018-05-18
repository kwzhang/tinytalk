package com.lge.architect.tinytalk.voicecall;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

public class VoiceCallService extends IntentService {
  public VoiceCallService() {
    super("VoiceCallService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      final String action = intent.getAction();

    }
  }
}
