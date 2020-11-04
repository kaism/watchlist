package com.github.kaism.watchlist.ui.stocks;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.kaism.watchlist.R;
import com.github.kaism.watchlist.db.Stock;
import com.github.kaism.watchlist.utils.Utils;

public class EditStockActivity extends AppCompatActivity {
	// bundle key constants
	public static final String SYMBOL = "com.github.kaism.watchlist.SYMBOL";

	Stock mStock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_stock);

		// get symbol
		final String symbol;
		if (savedInstanceState != null) {
			symbol = savedInstanceState.getString(SYMBOL);
		} else {
			symbol = getIntent().getStringExtra(SYMBOL);
		}

		// set symbol text
		TextView symbolTextView = findViewById(R.id.symbol);
		symbolTextView.setText(symbol);

		// initialize views
		final TextView lowPriceEditText = findViewById(R.id.lowPrice);
		final TextView highPriceEditText = findViewById(R.id.highPrice);

		// set up view model and observer
		final StockViewModel stockViewModel = new ViewModelProvider(this).get(StockViewModel.class);
		stockViewModel.getStockBySymbolLiveData(symbol).observe(this, new Observer<Stock>() {
			@Override
			public void onChanged(Stock stock) {
				if (stock == null) { return; }
				mStock = stock;
				if (stock.getLowPrice() > 0) {
					lowPriceEditText.setHint(null);
					lowPriceEditText.setText(Utils.priceToString(stock.getLowPrice()));
				}
				if (stock.getHighPrice() > 0) {
					lowPriceEditText.setHint(null);
					highPriceEditText.setText(Utils.priceToString(stock.getHighPrice()));
				}
			}
		});

		// handle save stock
		findViewById(R.id.save_stock).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mStock == null) { mStock = new Stock(symbol); }
				mStock.setLowPrice(lowPriceEditText.getText().toString());
				mStock.setHighPrice(highPriceEditText.getText().toString());
				stockViewModel.save(mStock);
				finish();
			}
		});
	}

}