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

import com.lge.architect.tinytalk.settings.SettingsActivity;
import com.lge.architect.tinytalk.util.NetworkUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class InCallService extends JobService implements AudioManager.OnAudioFocusChangeListener {
  private static final String TAG = InCallService.class.getSimpleName();

  public static final int CALL_JOB_ID = 101;
  public static final int PEER_JOIN_JOB_ID = 102;
  public static final int PEER_LEAVE_JOB_ID = 103;

  private static final int START_UDP_PORT = 5000;

  private static final String EXTRA_CONFERENCE_ID = "CONFERENCE_ID";
  private static final String EXTRA_PEER_ADDRESSES = "PEER_ADDRESSES";
  private static final String EXTRA_PEER_ADDRESS = "PEER_ADDRESS";
  private static final String EXTRA_CODEC = "CODEC";
  private static final String EXTRA_TRANSPORT = "TRANSPORT";

  private VoIPAudio audio;

  public static void startCall(Context context, String remoteAddress /* TODO: String codec, String transport */) {
    JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

    if (jobScheduler != null) {
      PersistableBundle extras = new PersistableBundle();
      extras.putStringArray(InCallService.EXTRA_PEER_ADDRESSES, new String[] {remoteAddress});

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

  public static void startConferenceCall(Context context, String conferenceId, List<String> peerAddresses, String codec, String transport) {
    JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

    if (jobScheduler != null) {
      Collections.sort(peerAddresses);
      PersistableBundle extras = new PersistableBundle();
      extras.putString(InCallService.EXTRA_CONFERENCE_ID, conferenceId);
      extras.putStringArray(InCallService.EXTRA_PEER_ADDRESSES, peerAddresses.toArray(new String[0]));
      extras.putString(InCallService.EXTRA_CODEC, codec);
      extras.putString(InCallService.EXTRA_TRANSPORT, transport);

      jobScheduler.schedule(new JobInfo.Builder(CALL_JOB_ID, new ComponentName(context, InCallService.class))
          .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
          .setExtras(extras)
          .build());
    }
  }

  public static void addPeerToConferenceCall(Context context, String conferenceId, String peerAddress) {
    JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

    if (jobScheduler != null) {
      PersistableBundle extras = new PersistableBundle();
      extras.putString(InCallService.EXTRA_CONFERENCE_ID, conferenceId);
      extras.putString(InCallService.EXTRA_PEER_ADDRESS, peerAddress);

      jobScheduler.schedule(new JobInfo.Builder(PEER_JOIN_JOB_ID, new ComponentName(context, InCallService.class))
          .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
          .setExtras(extras)
          .build());
    }
  }

  public static void leavePeerFromConferenceCall(Context context, String conferenceId, String peerAddress) {
    JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

    if (jobScheduler != null) {
      PersistableBundle extras = new PersistableBundle();
      extras.putString(InCallService.EXTRA_CONFERENCE_ID, conferenceId);
      extras.putString(InCallService.EXTRA_PEER_ADDRESS, peerAddress);

      jobScheduler.schedule(new JobInfo.Builder(PEER_LEAVE_JOB_ID, new ComponentName(context, InCallService.class))
          .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
          .setExtras(extras)
          .build());
    }
  }

  @Override
  public boolean onStartJob(JobParameters params) {
    PersistableBundle extras = params.getExtras();
    ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    switch (params.getJobId()) {
      case CALL_JOB_ID:
        executor.execute(() -> {
          String[] addresses = extras.getStringArray(EXTRA_PEER_ADDRESSES);
          if (addresses != null && addresses.length > 0) {
            audio = VoIPAudio.getInstance(getApplicationContext());

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int jitterDelay = Integer.parseInt(preferences.getString(SettingsActivity.KEY_EXPERIMENT_JITTER_DELAY, "180"));
            int port = START_UDP_PORT + addresses.length - 1;

            List<InetAddress> addressList = Arrays.stream(addresses)
                .map(NetworkUtil::toInetAddress)
                .sorted(NetworkUtil.ADDRESS_COMPARATOR)
                .collect(Collectors.toList());

            for (int index = 0; index < addressList.size(); ++index) {
              audio.startAudio(addressList.get(index), port + index, jitterDelay);
            }
          }
        });
        break;
      case PEER_JOIN_JOB_ID:
        executor.execute(() -> {
          InetAddress address = NetworkUtil.toInetAddress(extras.getString(EXTRA_PEER_ADDRESS));
          SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
          VoIPAudio audio = VoIPAudio.getInstance(getApplicationContext());

          InetAddress localAddress = NetworkUtil.getLocalIpAddress();
          TreeSet<InetAddress> addressSet = new TreeSet<>(NetworkUtil.ADDRESS_COMPARATOR);
          addressSet.addAll(audio.getPeerAddresses());
          addressSet.add(localAddress);

          int port = START_UDP_PORT + audio.getPeerSize();
          InetAddress lowerAddress = addressSet.lower(localAddress);
          while (lowerAddress != null) {
            port++;
            addressSet.remove(lowerAddress);
            lowerAddress = addressSet.lower(localAddress);
          }

          audio = VoIPAudio.getInstance(getApplicationContext());
          audio.startAudio(address, port,
              Integer.parseInt(preferences.getString(SettingsActivity.KEY_EXPERIMENT_JITTER_DELAY, "180")));
        });
        break;

      case PEER_LEAVE_JOB_ID:
        executor.execute(() -> {
          try {
            InetAddress address = InetAddress.getByName(extras.getString(EXTRA_PEER_ADDRESS));

            audio = VoIPAudio.getInstance(getApplicationContext());
            audio.endAudio(address);
          } catch (UnknownHostException e) {
            e.printStackTrace();
          }
        });
        break;
    }

    return true;
  }

  @Override
  public boolean onStopJob(JobParameters params) {
    if (params.getJobId() == CALL_JOB_ID) {
      if (audio != null) {
        audio.endAudio();
      }
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
