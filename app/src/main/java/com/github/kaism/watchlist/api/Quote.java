package com.github.kaism.watchlist.api;

import com.google.gson.annotations.SerializedName;

public class Quote {
	@SerializedName("quote") private QuoteInfo quoteInfo;
	public String getPrice() { return quoteInfo.getLatestPrice(); }

	static class QuoteInfo {
		@SerializedName("latestPrice") private String latestPrice;
		String getLatestPrice() { return latestPrice; }
	}
}
