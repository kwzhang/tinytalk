package com.lge.architect.tinytalk.voicecall;

import java.util.Observable;

public class PhoneState extends Observable {
  public enum CallState {LISTENING, CALLING, INCOMING, INCALL}

  private CallState callState = CallState.LISTENING;
  private String remoteIP;
  private String localIP;
  private boolean ringerEnabled;
  private boolean micEnabled;

  private static PhoneState instance = new PhoneState();

  public static PhoneState getInstance() {
    return instance;
  }

  public PhoneState() {
  }

  public void setPhoneState(CallState callstate) {
    callState = callstate;
  }

  public CallState getPhoneState() {
    return callState;
  }

  public void setRemoteIP(String value) {
    remoteIP = value;
  }

  public String getRemoteIP() {
    return remoteIP;
  }

  public void setLocalIP(String value) {
    localIP = value;
  }

  public String getLocalIP() {
    return localIP;
  }

  public void setRinger(boolean value) {
    ringerEnabled = value;
  }

  public boolean getRinger() {
    return ringerEnabled;
  }

  public void setMic(boolean value) {
    micEnabled = value;
  }

  public boolean getMic() {
    return micEnabled;
  }

  public void notifyUpdate() {
    setChanged();
    notifyObservers();
  }
}
