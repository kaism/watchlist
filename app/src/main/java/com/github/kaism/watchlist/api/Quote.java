package com.github.kaism.watchlist.api;

import com.google.gson.annotations.SerializedName;

public class Quote {
	@SerializedName("quote") private QuoteInfo quoteInfo;
	private String symbol = "";

	public void setSymbol(String symbol) { this.symbol = symbol; }

	public String getSymbol() { return symbol; }
	public String getPrice() {
		if (quoteInfo == null) return "";
		return quoteInfo.getLatestPrice();
	}

	private static class QuoteInfo {
		@SerializedName("latestPrice") private String latestPrice;
		String getLatestPrice() { return latestPrice; }
	}
}
