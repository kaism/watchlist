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

	StockListAdapter(Context context) {
		layoutInflater = LayoutInflater.from(context);
	}

	@NonNull
	@Override
	public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new StockViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
		if (stocks != null) {
			// set symbol view
			Stock current = stocks.get(position);
			holder.symbolView.setText(current.getSymbol());

			// calculate price views
			int lowPrice = current.getLowPrice();
			int highPrice = current.getHighPrice();
			int midPrice = getAverage(lowPrice, highPrice);
			int price2 = getAverage(lowPrice, midPrice);
			int price6 = getAverage(midPrice, highPrice);
			int price1 = getAverage(lowPrice, price2);
			int price3 = getAverage(price1, midPrice);
			int price5 = getAverage(midPrice, price6);
			int price7 = getAverage(price5, highPrice);

			// set price views
			holder.lowPriceView.setText(priceToString(lowPrice));
			holder.price1View.setText(priceToString(price1));
			holder.price2View.setText(priceToString(price2));
			holder.price3View.setText(priceToString(price3));
			holder.midPriceView.setText(priceToString(midPrice));
			holder.price5View.setText(priceToString(price5));
			holder.price6View.setText(priceToString(price6));
			holder.price7View.setText(priceToString(price7));
			holder.highPriceView.setText(priceToString(highPrice));
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
			symbolView = itemView.findViewById(R.id.symbol);
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
		return (lowPrice+highPrice)/2;
	}

	private String priceToString(int price) {
		int cents = Math.abs(price) % 100;
		int dollars = (price-cents)/100;
		return dollars + "." + cents;
	}

}
