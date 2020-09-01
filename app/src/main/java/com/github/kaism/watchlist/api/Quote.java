package com.github.kaism.watchlist.api;

import com.google.gson.annotations.SerializedName;

public class Quote {
	@SerializedName("quote") private QuoteInfo quoteInfo;
	public String getSymbol() { return quoteInfo.getSymbol(); }
	public float getPrice() { return quoteInfo.getLatestPrice(); }

	static class QuoteInfo {
		@SerializedName("symbol") private String symbol;
		@SerializedName("latestPrice") private float latestPrice;
		String getSymbol() { return symbol; }
		float getLatestPrice() { return latestPrice; }
	}
}
