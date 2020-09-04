package com.github.kaism.watchlist;

public class Utils {

	public static int stringToPrice(String string) {
		if (string != null){
			String[] arr = string.split("\\.");
			if (arr.length == 1) {
				return Integer.parseInt(arr[0].concat("00"));
			} else if (arr.length == 2) {
				return Integer.parseInt(arr[0].concat(arr[1]));
			}
		}
		return 0;
	}

}
