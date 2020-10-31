package com.github.kaism.watchlist.utils;

import com.github.kaism.watchlist.db.Stock;

import java.util.List;
import java.util.Locale;

public class Utils {

	public static String getSymbolsCsv(List<Stock> stocks) {
		if (stocks.size() == 0) return "";
		StringBuilder stringBuilder = new StringBuilder();
		for (Stock stock : stocks) {
			stringBuilder.append(stock.getSymbol().toLowerCase()).append(",");
		}
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
		return stringBuilder.toString();
	}

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
