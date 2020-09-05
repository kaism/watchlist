package com.github.kaism.watchlist;

import android.annotation.SuppressLint;

public class Utils {

	public static int stringToPrice(String string) {
		if (string != null && !string.equals("")){
			try {
				return (int) Math.round((Float.parseFloat(string) * 100));
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		return 0;
	}

	@SuppressLint("DefaultLocale")
	public static String priceToString(int price) {
		return String.format("%.2f", (float) price/100);
	}

}
