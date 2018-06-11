package com.lge.architect.tinytalk.command.model;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class DialResponse {
  public enum Type {
    ACCEPT, BUSY, DENY
  }

  public static final String URI = "dialresponse/{type}";

  protected String receiverIp;

  public DialResponse() {
    InetAddress inetAddress = getLocalIpAddress();

    if (inetAddress != null) {
      receiverIp = inetAddress.toString();
    } else {
      receiverIp = "rtsp://0.0.0.0";
    }
  }

  private static InetAddress getLocalIpAddress() {
    InetAddress foundAddress = null;
    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
        NetworkInterface networkInterface = en.nextElement();
        for (Enumeration<InetAddress> address = networkInterface.getInetAddresses(); address.hasMoreElements(); ) {
          InetAddress inetAddress = address.nextElement();

          if (!inetAddress.isLoopbackAddress() && "wlan0".equals(networkInterface.getName()) && !inetAddress.getHostAddress().startsWith("fe80")) {
            foundAddress = inetAddress;
            if (inetAddress instanceof Inet4Address) {
              return foundAddress;
            }
          }
        }
      }
    } catch (SocketException e) {
      e.printStackTrace();
    }
    return foundAddress;
  }
}
