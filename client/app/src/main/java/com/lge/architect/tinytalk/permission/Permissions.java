package com.lge.architect.tinytalk.permission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class Permissions {
  public static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
  public static final int REQUEST_ACCESS_COARSE_LOCATION = 2;
  public static final int REQUEST_RECORD_AUDIO = 3;
  public static final int REQUEST_FOR_ALL = 4;

  public static final String[] PERMISSIONS = {
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.ACCESS_COARSE_LOCATION,
      Manifest.permission.RECORD_AUDIO,
  };

  public static int getRequestCode(String permission) {
    switch (permission) {
      case Manifest.permission.ACCESS_COARSE_LOCATION:
        return REQUEST_ACCESS_COARSE_LOCATION;
      case Manifest.permission.READ_EXTERNAL_STORAGE:
        return REQUEST_READ_EXTERNAL_STORAGE;
      case Manifest.permission.RECORD_AUDIO:
        return REQUEST_RECORD_AUDIO;
      default:
        return REQUEST_FOR_ALL;
    }
  }

  @SuppressLint("ObsoleteSdkInt")
  public static boolean checkReadExternalStorage(Context context) {
    return (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED);
  }

  public static boolean checkCoarseLocation(Context context) {
    return (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED);
  }

  public static boolean checkRecordAudio(Context context) {
    return (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.RECORD_AUDIO)
        == PackageManager.PERMISSION_GRANTED);
  }
}
