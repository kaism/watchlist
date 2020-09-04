package com.github.kaism.watchlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.kaism.watchlist.api.ApiClient;
import com.github.kaism.watchlist.api.ApiInterface;
import com.github.kaism.watchlist.api.Quote;
import com.github.kaism.watchlist.db.Stock;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
	private static final int NEW_STOCK_ACTIVITY_REQUEST_CODE = 1;
	private StockViewModel stockViewModel;
	SwipeRefreshLayout mSwipeRefreshLayout;
	private ApiInterface apiInterface;
	private String symbolsCsv = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// set up recycler view
		final StockListAdapter adapter = new StockListAdapter(this);
		RecyclerView recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		// set up view model and observer
		stockViewModel = ViewModelProviders.of(this).get(StockViewModel.class);
		stockViewModel.getStocks().observe(this, new Observer<List<Stock>>() {
			@Override
			public void onChanged(List<Stock> stocks) {
				adapter.setStocks(stocks);
				if (adapter.getItemCount() > 0) {
					findViewById(R.id.empty_text).setVisibility(View.GONE);
					symbolsCsv = getStocksCsv(stocks);
				}
				Toast.makeText(MainActivity.this, symbolsCsv, Toast.LENGTH_SHORT).show();
			}
		});

		setOnClickListeners();

		// set delete item action with swipe
		ItemTouchHelper helper = new ItemTouchHelper(
				new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
					@Override
					public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
										  @NonNull RecyclerView.ViewHolder target) {
						return false;
					}

					@Override
					public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
						Stock stock = adapter.getStockAtPosition(viewHolder.getAdapterPosition());
						confirmDelete(stock);
					}
				});
		helper.attachToRecyclerView(recyclerView);

		// set up swipe to refresh
		final MainActivity activity = this;
		mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new Refresh(activity).execute();
			}
		});

		// configure api
		apiInterface = ApiClient.getClient().create(ApiInterface.class);

	}

	private String getStocksCsv(List<Stock> stocks) {
		StringBuilder str = new StringBuilder();
		for (Stock stock : stocks) {
			str.append(stock.getSymbol().toLowerCase()).append(",");
		}
		str.deleteCharAt(str.length()-1);
		return str.toString();
	}

	private void setOnClickListeners() {
		// handle add stock button
		findViewById(R.id.button_add_stock).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivityForResult(
						new Intent(MainActivity.this, AddStockActivity.class),
						NEW_STOCK_ACTIVITY_REQUEST_CODE
				);
			}
		});
	}

	private void getQuotes() {
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
		if (requestCode == NEW_STOCK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
			String symbol = data.getStringExtra(AddStockActivity.SYMBOL);
			int lowPrice = data.getIntExtra(AddStockActivity.LOW_PRICE, 0);
			int highPrice = data.getIntExtra(AddStockActivity.HIGH_PRICE, 0);
			if (symbol != null && !symbol.equals("")) {
				Stock stock = new Stock(symbol);
				stock.setLowPrice(lowPrice);
				stock.setHighPrice(highPrice);
				stockViewModel.insert(stock);
				Toast.makeText(getApplicationContext(), symbol + " added", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
			}
		}
	}

	private void confirmDelete(final Stock stock) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setMessage(getString(R.string.dialog_question_confirm_delete_stock))
				.setPositiveButton(R.string.dialog_button_delete, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(MainActivity.this, "Deleting " + stock.getSymbol(), Toast.LENGTH_LONG).show();
						stockViewModel.delete(stock);
					}
				})
				.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_LONG).show();
					}
				});
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
			activity.mSwipeRefreshLayout.setRefreshing(false);
			if (success) {
				Toast.makeText(activity, "Success!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
			}
		}
	}

}