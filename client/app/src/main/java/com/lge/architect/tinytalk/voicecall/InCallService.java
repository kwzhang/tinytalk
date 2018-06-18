package com.lge.architect.tinytalk.voicecall;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InCallService extends JobService implements AudioManager.OnAudioFocusChangeListener {
  private static final String TAG = InCallService.class.getSimpleName();

  public static final int CALL_JOB_ID = 101;
  public static final int MUTE_JOB_ID = 102;

  public static final String EXTRA_REMOTE_ADDRESS = "EXTRA_REMOTE_ADDRESS";
  public static final String EXTRA_MUTE_MIC = "EXTRA_MUTE_MIC";

  private VoIPAudio audio;

  public static void startCall(Context context, String remoteAddress) {
    JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

    if (jobScheduler != null) {
      PersistableBundle extras = new PersistableBundle();
      extras.putString(InCallService.EXTRA_REMOTE_ADDRESS, remoteAddress);

      jobScheduler.schedule(new JobInfo.Builder(CALL_JOB_ID, new ComponentName(context, InCallService.class))
          .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
          .setExtras(extras)
          .build());
    }
  }

  public static void stopCall(Context context) {
    JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

    if (jobScheduler != null) {
      jobScheduler.cancel(CALL_JOB_ID);
    }
  }

  public static void muteMicrophone(Context context, boolean isMute) {
    JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

    if (jobScheduler != null) {
      PersistableBundle extras = new PersistableBundle();
      extras.putBoolean(InCallService.EXTRA_MUTE_MIC, isMute);

      jobScheduler.schedule(new JobInfo.Builder(MUTE_JOB_ID, new ComponentName(context, InCallService.class))
          .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
          .setExtras(extras)
          .build());
    }
  }

  @Override
  public boolean onStartJob(JobParameters params) {
    PersistableBundle extras = params.getExtras();

    switch (params.getJobId()) {
      case CALL_JOB_ID:
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

        executor.execute(() -> {
          try {
            InetAddress address = InetAddress.getByName(extras.getString(EXTRA_REMOTE_ADDRESS));
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

            audio = VoIPAudio.getInstance(getApplicationContext());
            audio.startAudio(address, Integer.parseInt(preferences.getString("simulated_voice", "0")));
          } catch (UnknownHostException e) {
            e.printStackTrace();
          }
        });
        break;

      case MUTE_JOB_ID:
        if (audio != null) {
          audio.muteAudio(extras.getBoolean(EXTRA_MUTE_MIC, false));
        }
        jobFinished(params, false);
        break;
    }

    return true;
  }

  @Override
  public boolean onStopJob(JobParameters params) {
    if (audio != null) {
      audio.endAudio();
    }

    return false;
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onAudioFocusChange(int focusChange) {
    switch (focusChange) {
      case AudioManager.AUDIOFOCUS_LOSS:
        stopSelf();
        break;
      case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
        stopSelf();
        break;
    }
  }
}
