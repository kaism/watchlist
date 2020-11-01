package com.github.kaism.watchlist.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.github.kaism.watchlist.BuildConfig;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCalls {

	public static Call<Map<String, Quote>> getQuotes(String csvSymbols) {
		return new GetQuotes(csvSymbols);
	}

	private static class GetQuotes implements Call<Map<String, Quote>> {
		ApiInterface apiInterface;
		String csvSymbols;

		public GetQuotes(String csvSymbols) {
			apiInterface = ApiClient.getClient().create(ApiInterface.class);
			this.csvSymbols = csvSymbols;
		}

		@Override
		public void enqueue(@NonNull Callback<Map<String, Quote>> callback) {
			Log.d("KDBUG", "url: "+ BuildConfig.QUOTEAPI_URL+"batch?types=quote&filter=latestPrice&token="+BuildConfig.QUOTEAPI_KEY+"&symbols="+csvSymbols);
			Call<Map<String, Quote>> call = apiInterface.getQuotes(csvSymbols);
			call.enqueue(callback);
		}

		@Override public @NonNull Response<Map<String, Quote>> execute() { return null; }
		@Override public boolean isExecuted() { return false; }
		@Override public void cancel() {}
		@Override public boolean isCanceled() { return false; }
		@Override public @NonNull Request request() { return null; }

		@Override
		public @NonNull Call<Map<String, Quote>> clone() {
			try { super.clone(); } catch (CloneNotSupportedException e) { e.printStackTrace(); }
			return null;
		}

	}

	public static class QuotesCallback implements Callback<Map<String, Quote>> {

		/**
		 * Callback method to be invoked when quotes have been received.
		 *
		 * @param quotes quotes received from api
		 */
		public void onQuotesReceived(ArrayList<Quote> quotes) {}

		@Override
		public void onResponse(@NonNull Call<Map<String, Quote>> call, @NonNull Response<Map<String, Quote>> response) {
			ArrayList<Quote> quotes = new ArrayList<>();

			Map<String, Quote> stringQuoteMap = response.body();
			if (stringQuoteMap != null && !stringQuoteMap.isEmpty()) {
				for (Map.Entry<String, Quote> entry : stringQuoteMap.entrySet()) {
					Quote quote = entry.getValue();
					quote.setSymbol(entry.getKey());
					if (!quote.getSymbol().equals("") && !quote.getPrice().equals("")) {
						quotes.add(quote);
					}
				}
			}
			if (quotes == null) {
				Log.d("KDBUG", "onQuotesReceived: no quotes");
			} else {
				onQuotesReceived(quotes);
			}
		}

		@Override
		public void onFailure(@NonNull Call<Map<String, Quote>> call, @NonNull Throwable t) {
			Log.d("KDBUG", "onFailure: api response failure");
		}
	}

}
