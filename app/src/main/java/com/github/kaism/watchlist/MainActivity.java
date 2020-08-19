package com.github.kaism.watchlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.kaism.watchlist.db.Stock;

import java.util.List;


public class MainActivity extends AppCompatActivity {
	private static final int NEW_STOCK_ACTIVITY_REQUEST_CODE = 1;
	private StockViewModel stockViewModel;

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
			}
		});

		setOnClickListeners();
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
			if (symbol != null && !symbol.equals("")) {
				Stock stock = new Stock(symbol);
				stockViewModel.insert(stock);
				Toast.makeText(getApplicationContext(), symbol + " added", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
			}
		}
	}
}