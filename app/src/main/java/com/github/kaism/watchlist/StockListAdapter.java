package com.github.kaism.watchlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.kaism.watchlist.db.Stock;

import java.util.List;


public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockViewHolder> {
	private final LayoutInflater layoutInflater;
	private List<Stock> stocks;
	private Context context;

	StockListAdapter(Context context) {
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
			int lowPrice = current.getLowPrice();
			int currentPrice = current.getCurrentPrice();
			int highPrice = current.getHighPrice();

			// set text views
			holder.symbolView.setText(current.getSymbol());
			holder.lowPriceTextView.setText(priceToString(lowPrice));
			holder.currentPriceTextView.setText(priceToString(currentPrice));
			holder.highPriceTextView.setText(priceToString(highPrice));

			// calculate background views
			int midPrice = getAverage(lowPrice, highPrice);
			int price2 = getAverage(lowPrice, midPrice);
			int price6 = getAverage(midPrice, highPrice);
			int price1 = getAverage(lowPrice, price2);
			int price3 = getAverage(price1, midPrice);
			int price5 = getAverage(midPrice, price6);
			int price7 = getAverage(price5, highPrice);

			// set background colors based on price
			holder.lowPriceView.setBackgroundColor(getColor(currentPrice, lowPrice));
			holder.price1View.setBackgroundColor(getColor(currentPrice, price1));
			holder.price2View.setBackgroundColor(getColor(currentPrice, price2));
			holder.price3View.setBackgroundColor(getColor(currentPrice, price3));
			holder.midPriceView.setBackgroundColor(getColor(currentPrice, midPrice));
			holder.price5View.setBackgroundColor(getColor(currentPrice, price5));
			holder.price6View.setBackgroundColor(getColor(currentPrice, price6));
			holder.price7View.setBackgroundColor(getColor(currentPrice, price7));
			holder.highPriceView.setBackgroundColor(getColor(currentPrice, highPrice));
		}
	}

	void setStocks(List<Stock> stocks) {
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
		// text views
		private final TextView lowPriceTextView;
		private final TextView currentPriceTextView;
		private final TextView highPriceTextView;

		// background views
		private final TextView symbolView;
		private final TextView lowPriceView;
		private final TextView price1View;
		private final TextView price2View;
		private final TextView price3View;
		private final TextView midPriceView;
		private final TextView price5View;
		private final TextView price6View;
		private final TextView price7View;
		private final TextView highPriceView;

		private StockViewHolder(@NonNull View itemView) {
			super(itemView);

			// text views
			symbolView = itemView.findViewById(R.id.symbol);
			lowPriceTextView = itemView.findViewById(R.id.lowPriceText);
			currentPriceTextView = itemView.findViewById(R.id.currentPriceText);
			highPriceTextView = itemView.findViewById(R.id.highPriceText);

			// background views
			lowPriceView = itemView.findViewById(R.id.lowPrice);
			price1View = itemView.findViewById(R.id.price1);
			price2View = itemView.findViewById(R.id.price2);
			price3View = itemView.findViewById(R.id.price3);
			midPriceView = itemView.findViewById(R.id.midPrice);
			price5View = itemView.findViewById(R.id.price5);
			price6View = itemView.findViewById(R.id.price6);
			price7View = itemView.findViewById(R.id.price7);
			highPriceView = itemView.findViewById(R.id.highPrice);
		}
	}

	private int getAverage(int lowPrice, int highPrice) {
		return (lowPrice + highPrice) / 2;
	}

	private String priceToString(int price) {
		int cents = Math.abs(price) % 100;
		int dollars = (price - cents) / 100;
		return dollars + "." + cents;
	}

	private int getColor(int currentPrice, int viewPrice) {
		if (currentPrice >= viewPrice)
			return context.getResources().getColor(R.color.green);
		else
			return context.getResources().getColor(R.color.red);

	}

}
