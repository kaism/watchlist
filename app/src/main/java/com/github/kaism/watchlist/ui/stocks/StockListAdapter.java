package com.github.kaism.watchlist.ui.stocks;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.kaism.watchlist.R;
import com.github.kaism.watchlist.db.Stock;

import java.util.List;

import static com.github.kaism.watchlist.utils.Utils.priceToString;


public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockViewHolder> {
	private final LayoutInflater layoutInflater;
	private String TAG = "KDBUG";
	private List<Stock> stocks;

	// color resources
	private int bgColorGreen;
	private int bgColorRed;
	private int bgColorTransparent;

	/**
	 * Callback method to be invoked when RecyclerView item is clicked.
	 */
	public void onItemClicked(Stock stock) {}

	public StockListAdapter(Context context) {
		layoutInflater = LayoutInflater.from(context);

		// get color resources
		this.bgColorGreen = context.getResources().getColor(R.color.green);
		this.bgColorRed = context.getResources().getColor(R.color.red);
		this.bgColorTransparent = context.getResources().getColor(R.color.transparent);
	}

	@NonNull
	@Override
	public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new StockViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull StockViewHolder holder, final int position) {
		if (stocks != null) {
			Stock current = stocks.get(position);
			int lowPrice = current.getLowPrice();
			int currentPrice = current.getCurrentPrice();
			int highPrice = current.getHighPrice();

			// populate text
			holder.symbolTextView.setText(current.getSymbol());
			holder.lowPriceTextView.setText(priceToString(lowPrice));
			holder.currentPriceTextView.setText(priceToString(currentPrice));
			holder.highPriceTextView.setText(priceToString(highPrice));

			// set progress bar
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { holder.progressBar.setMin(0); }
			holder.progressBar.setMax(highPrice - lowPrice);
			holder.progressBar.setProgress(currentPrice - lowPrice);

			// set background color
			if (currentPrice != 0 && currentPrice <= lowPrice) {
				holder.symbolTextView.setBackgroundColor(bgColorRed);
			} else if (currentPrice >= highPrice) {
				holder.symbolTextView.setBackgroundColor(bgColorGreen);
			} else {
				holder.symbolTextView.setBackgroundColor(bgColorTransparent);
			}

			// set click handler to the item
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d(TAG, "onClick: position "+position);
					onItemClicked(getStockAtPosition(position));
				}
			});
		}
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
		notifyDataSetChanged();
	}

	public Stock getStockAtPosition(int position) {
		return stocks.get(position);
	}

	@Override
	public int getItemCount() {
		if (stocks != null) {
			return stocks.size();
		}
		return 0;
	}

	static class StockViewHolder extends RecyclerView.ViewHolder {
		private final TextView symbolTextView;
		private final TextView lowPriceTextView;
		private final TextView currentPriceTextView;
		private final TextView highPriceTextView;
		private final ProgressBar progressBar;

		private StockViewHolder(@NonNull View itemView) {
			super(itemView);
			symbolTextView = itemView.findViewById(R.id.symbol);
			lowPriceTextView = itemView.findViewById(R.id.lowPriceText);
			currentPriceTextView = itemView.findViewById(R.id.currentPriceText);
			highPriceTextView = itemView.findViewById(R.id.highPriceText);
			progressBar = itemView.findViewById(R.id.progressBar);
		}
	}
}
