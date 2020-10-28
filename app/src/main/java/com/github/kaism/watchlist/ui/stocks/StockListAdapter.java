package com.github.kaism.watchlist.ui.stocks;

import android.content.Context;
import android.os.Build;
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
	private List<Stock> stocks;
	private Context context;

	public StockListAdapter(Context context) {
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
	}

	@NonNull
	@Override
	public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new StockViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
		if (stocks != null) {
			Stock current = stocks.get(position);
			holder.symbolTextView.setText(current.getSymbol());
			int lowPrice = current.getLowPrice();
			int currentPrice = current.getCurrentPrice();
			int highPrice = current.getHighPrice();

			holder.lowPriceTextView.setText(priceToString(lowPrice));
			holder.currentPriceTextView.setText(priceToString(currentPrice));
			holder.highPriceTextView.setText(priceToString(highPrice));

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				holder.progressBar.setMin(0);
			}
			holder.progressBar.setMax(highPrice - lowPrice);
			holder.progressBar.setProgress(currentPrice - lowPrice);

			if (currentPrice != 0 && currentPrice <= lowPrice) {
				holder.symbolTextView.setBackgroundColor(context.getResources().getColor(R.color.red));
			} else if (currentPrice >= highPrice) {
				holder.symbolTextView.setBackgroundColor(context.getResources().getColor(R.color.green));
			} else {
				holder.symbolTextView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
			}
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
