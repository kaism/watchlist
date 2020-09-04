package com.github.kaism.watchlist.api;

import com.github.kaism.watchlist.BuildConfig;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
	@GET("batch?types=quote&filter=latestPrice&token=" + BuildConfig.QUOTEAPI_KEY)
	Call<Map<String, Quote>> getQuotes(@Query("symbols") String symbolsCsv);
}
