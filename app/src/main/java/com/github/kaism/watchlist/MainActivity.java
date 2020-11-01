package com.github.kaism.watchlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.github.kaism.watchlist.utils.ConfirmDeleteDialog;
import com.github.kaism.watchlist.utils.ListItemTouchHelper;
import com.github.kaism.watchlist.utils.RefreshListener;
import com.github.kaism.watchlist.utils.ScrollListener;
import com.github.kaism.watchlist.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
	private static final int REQUEST_CODE_NEW_STOCK = 100;
	private StockViewModel stockViewModel;
	public SwipeRefreshLayout swipeRefreshLayout;
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
				final Stock stock = adapter.getStockAtPosition(position);
				new ConfirmDeleteDialog(stock, MainActivity.this) {
					@Override
					public void onConfirm() {
						stockViewModel.delete(stock);
					}
				}.show();
				adapter.notifyItemChanged(position);
			}
		}).attachToRecyclerView(recyclerView);

		// set up view model and observer
		stockViewModel = new ViewModelProvider(this).get(StockViewModel.class);
		stockViewModel.getAllStocksLiveData().observe(this, new Observer<List<Stock>>() {
			@Override
			public void onChanged(List<Stock> stocks) {
				adapter.setStocks(stocks);
				symbolsCsv = Utils.getSymbolsCsv(stocks);
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
				stockViewModel.addSymbols(BuildConfig.QUICKSEED_CSV);
				return true;
			}
		});

		// set up swipe to refresh
		swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
		swipeRefreshLayout.setOnRefreshListener(new RefreshListener(this) {
			@Override
			public void onRefreshDoInBackground() {
				getQuotes();
			}

			@Override
			public void onRefreshPostExecute(boolean success) {
				swipeRefreshLayout.setRefreshing(false);
				Context context = getApplicationContext();
				if (success) {
					Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
				}
			}
		});




		// configure api
		apiInterface = ApiClient.getClient().create(ApiInterface.class);

	}














	public void getQuotes() {
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

}