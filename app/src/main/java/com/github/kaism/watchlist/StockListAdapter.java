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
			Stock current = stocks.get(position);
			holder.symbolView.setText(current.getSymbol());
		}
	}

	void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
		notifyDataSetChanged();
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

		private StockViewHolder(@NonNull View itemView) {
			super(itemView);
			symbolView = itemView.findViewById(R.id.symbol);
		}
	}
}
