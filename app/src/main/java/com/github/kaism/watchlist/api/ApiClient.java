package com.github.kaism.watchlist.api;

import com.github.kaism.watchlist.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
	private static final String API_BASE_URL = BuildConfig.QUOTEAPI_URL;
	private static Retrofit retrofit = null;

	public static Retrofit getClient() {
		if (retrofit == null) {
			retrofit = new Retrofit.Builder()
					.baseUrl(API_BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}
		return retrofit;
	}
}
