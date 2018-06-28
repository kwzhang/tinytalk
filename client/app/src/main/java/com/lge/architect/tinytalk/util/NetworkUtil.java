package com.lge.architect.tinytalk.util;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Map;

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

  public static final Comparator<InetAddress> ADDRESS_COMPARATOR = Comparator
      .comparing(InetAddress::getAddress,
          Comparator.comparingInt((byte[] b) -> b.length)
              .thenComparing(b -> new BigInteger(1, b)));

  public static String[] sort(final String[] addresses) {
    return Arrays.stream(addresses)
        .map(s -> new AbstractMap.SimpleImmutableEntry<>(toInetAddress(s), s))
        .sorted(Comparator.comparing(Map.Entry::getKey, Comparator.nullsLast(ADDRESS_COMPARATOR)))
        .map(Map.Entry::getValue)
        .toArray(String[]::new);
  }

  public static InetAddress toInetAddress(final String address) {
    try {
      return InetAddress.getByName(address);
    } catch (final UnknownHostException | SecurityException e) {
      e.printStackTrace();
      return null;
    }
  }
}
