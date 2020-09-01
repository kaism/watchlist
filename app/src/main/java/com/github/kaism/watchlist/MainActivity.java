package com.github.kaism.watchlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.github.kaism.watchlist.db.Stock;

import java.util.List;


public class MainActivity extends AppCompatActivity {
	private static final int NEW_STOCK_ACTIVITY_REQUEST_CODE = 1;
	private StockViewModel stockViewModel;

	SwipeRefreshLayout mSwipeRefreshLayout;

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
				}
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

		// set up pull to refresh
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				Toast.makeText(MainActivity.this, "REFRESHING...", Toast.LENGTH_LONG).show();
				mSwipeRefreshLayout.setRefreshing(false);
			}
		});

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

}