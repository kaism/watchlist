package com.github.kaism.watchlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.kaism.watchlist.api.ApiClient;
import com.github.kaism.watchlist.api.ApiInterface;
import com.github.kaism.watchlist.api.Quote;
import com.github.kaism.watchlist.db.Stock;
import com.github.kaism.watchlist.ui.stocks.AddStockActivity;
import com.github.kaism.watchlist.ui.stocks.StockListAdapter;
import com.github.kaism.watchlist.ui.stocks.StockViewModel;
import com.github.kaism.watchlist.utils.ListItemTouchHelper;
import com.github.kaism.watchlist.utils.ScrollListener;
import com.github.kaism.watchlist.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
	private static final int REQUEST_CODE_NEW_STOCK = 100;
	private StockViewModel stockViewModel;
	private SwipeRefreshLayout swipeRefreshLayout;
	private ApiInterface apiInterface;
	private String symbolsCsv = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// views that toggle visibility
		final TextView emptyTextView = findViewById(R.id.empty_text);
		final FloatingActionButton addStockButton = findViewById(R.id.button_add_stock);

		// set up recycler view and scroll action
		RecyclerView recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addOnScrollListener(new ScrollListener() {
			@Override
			public void onScrollUp() {
				addStockButton.setVisibility(View.GONE);
			}
			@Override
			public void onScrollDown() {
				addStockButton.setVisibility(View.VISIBLE);
			}
		});

		// set up adapter and item click action
		final StockListAdapter adapter = new StockListAdapter(this) {
			@Override
			public void onItemClicked(Stock stock) {
				Toast.makeText(MainActivity.this, stock.getSymbol()+" clicked!", Toast.LENGTH_SHORT).show();
			}
		};
		recyclerView.setAdapter(adapter);

		// set delete item action with swipe
		new ListItemTouchHelper(new ListItemTouchHelper.Callback() {
			@Override
			public void onSwipedLeft(int position) {
				confirmDelete(adapter.getStockAtPosition(position));
				adapter.notifyItemChanged(position);
			}
		}).attachToRecyclerView(recyclerView);

		// set up view model and observer
		stockViewModel = new ViewModelProvider(this).get(StockViewModel.class);
		stockViewModel.getAllStocksLiveData().observe(this, new Observer<List<Stock>>() {
			@Override
			public void onChanged(List<Stock> stocks) {
				adapter.setStocks(stocks);
				symbolsCsv = getSymbolsCsv(stocks);
				if (stocks.size() > 0) {
					emptyTextView.setVisibility(View.GONE);
				} else {
					emptyTextView.setVisibility(View.VISIBLE);
				}
			}
		});

		// handle add stock fab click
		addStockButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivityForResult(new Intent(MainActivity.this, AddStockActivity.class), REQUEST_CODE_NEW_STOCK);
			}
		});

		// handle add stock fab long click (quick seed)
		addStockButton.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				stockViewModel.seed();
				return true;
			}
		});













		// set up swipe to refresh
		final MainActivity activity = this;
		swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new Refresh(activity).execute();
			}
		});

		// configure api
		apiInterface = ApiClient.getClient().create(ApiInterface.class);

	}



	private void getQuotes() {
		Log.d("KDBUG", "url: "+BuildConfig.QUOTEAPI_URL+"batch?types=quote&filter=latestPrice&token="+BuildConfig.QUOTEAPI_KEY+"&symbols="+symbolsCsv);

		Call<Map<String, Quote>> call = apiInterface.getQuotes(symbolsCsv);
		call.enqueue(new Callback<Map<String, Quote>>() {
			@Override
			public void onResponse(@NonNull Call<Map<String, Quote>> call, @NonNull Response<Map<String, Quote>> response) {
				Map<String, Quote> quotes = response.body();
				if (quotes != null && quotes.size() > 0) {
					for (Map.Entry<String, Quote> entry : quotes.entrySet()) {
						stockViewModel.updatePrice(entry.getKey(), Utils.stringToPrice(entry.getValue().getPrice()));
					}
				} else {
					Toast.makeText(getApplicationContext(), R.string.error_no_quotes, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<Map<String, Quote>> call, @NonNull Throwable t) {
				Log.d("KDBUG", "onFailure: " + t.getMessage());
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// handle add new stock
		if (requestCode == REQUEST_CODE_NEW_STOCK && resultCode == RESULT_OK) {
			String symbol = data.getStringExtra(AddStockActivity.SYMBOL);
			int lowPrice = data.getIntExtra(AddStockActivity.LOW_PRICE, 0);
			int highPrice = data.getIntExtra(AddStockActivity.HIGH_PRICE, 0);
			if (symbol != null && !symbol.equals("")) {
				Stock stock = new Stock(symbol);
				stock.setLowPrice(lowPrice);
				stock.setHighPrice(highPrice);
				stockViewModel.save(stock);
				Toast.makeText(getApplicationContext(), symbol + " added", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
			}
		}
	}

	private void confirmDelete(final Stock stock) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setMessage(getString(R.string.delete)+" "+stock.getSymbol()+"?")
				.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						stockViewModel.delete(stock);
					}
				})
				.setNegativeButton(R.string.cancel, null);
		builder.show();
	}

	private static class Refresh extends AsyncTask<Void, Integer, Boolean> {
		private WeakReference<MainActivity> activityReference;
		Refresh(MainActivity activity) {
			activityReference = new WeakReference<>(activity);
		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			MainActivity activity = activityReference.get();
			if (activity == null || activity.isFinishing()) return false;
			activity.getQuotes();
			return true;
		}

		protected void onPostExecute(Boolean success) {
			MainActivity activity = activityReference.get();
			if (activity == null || activity.isFinishing()) return;

			// modify the activity's UI
			activity.swipeRefreshLayout.setRefreshing(false);
			if (success) {
				Toast.makeText(activity, "Success!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
			}
		}
	}




	private String getSymbolsCsv(List<Stock> stocks) {
		if (stocks.size() == 0) return "";
		StringBuilder stringBuilder = new StringBuilder();
		for (Stock stock : stocks) {
			stringBuilder.append(stock.getSymbol().toLowerCase()).append(",");
		}
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
		return stringBuilder.toString();
	}

}