package com.lge.architect.tinytalk.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtil {
  public static InetAddress getLocalIpAddress() {
    InetAddress foundAddress = null;
    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
        NetworkInterface networkInterface = en.nextElement();
        for (Enumeration<InetAddress> iterator = networkInterface.getInetAddresses(); iterator.hasMoreElements(); ) {
          InetAddress address = iterator.nextElement();

          if (!address.isLoopbackAddress() && "wlan0".equals(networkInterface.getName()) && !address.getHostAddress().startsWith("fe80")) {
            foundAddress = address;

            if (address instanceof Inet4Address) {
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
