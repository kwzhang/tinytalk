package com.lge.architect.tinytalk.voicecall.dialpad;

import android.telephony.PhoneNumberUtils;
import android.text.Spanned;
import android.text.method.DialerKeyListener;

public class UnicodeDialerKeyListener extends DialerKeyListener {
  public static final UnicodeDialerKeyListener INSTANCE = new UnicodeDialerKeyListener();

  @Override
  public CharSequence filter(CharSequence source, int start, int end,
                             Spanned dest, int dstart, int dend) {
    final String converted = PhoneNumberUtils.convertKeypadLettersToDigits(
        PhoneNumberUtils.replaceUnicodeDigits(source.toString()));
    CharSequence result = super.filter(converted, start, end, dest, dstart, dend);
    if (result == null) {
      if (source.equals(converted)) {

        return null;
      } else {
        return converted.subSequence(start, end);
      }
    }
    return result;
  }
}
