package com.designcraft.business.usage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Period {
	public static String currentPeriod() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		Date date = new Date();
		return dateFormat.format(date);
	}
}
