package com.github.kaism.watchlist;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.kaism.watchlist.db.Stock;
import com.github.kaism.watchlist.db.StockRepository;

import java.util.List;

public class StockViewModel extends AndroidViewModel {
	private StockRepository stockRepository;
	private LiveData<List<Stock>> stocks;

	public StockViewModel(Application application) {
		super(application);
		stockRepository = new StockRepository(application);
		stocks = stockRepository.getAllStocks();
	}

	public LiveData<List<Stock>> getStocks() {
		return stocks;
	}

	public void insert(Stock stock) {
		stockRepository.insert(stock);
	}

	public void updatePrice(String symbol, int price) {
		stockRepository.updatePrice(symbol, price);
	}

	public void delete(Stock stock) {
		stockRepository.delete(stock);
	}

}