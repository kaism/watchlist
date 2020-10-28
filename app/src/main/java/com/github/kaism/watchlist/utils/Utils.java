package com.github.kaism.watchlist.utils;

import java.util.Locale;

public class Utils {

	public static int stringToPrice(String string) {
		if (string != null && !string.equals("")){
			try {
				return Math.round((Float.parseFloat(string) * 100));
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		return 0;
	}

	public static String priceToString(int price) {
		return String.format(Locale.US, "%.2f", (float) price/100);
	}

}
