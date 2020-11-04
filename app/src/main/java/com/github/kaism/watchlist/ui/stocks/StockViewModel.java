package com.github.kaism.watchlist.ui.stocks;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.kaism.watchlist.db.Stock;
import com.github.kaism.watchlist.db.StockRepository;

import java.util.List;

public class StockViewModel extends AndroidViewModel {
	private StockRepository stockRepository;

	public StockViewModel(Application application) {
		super(application);
		stockRepository = new StockRepository(application);
	}

	public LiveData<List<Stock>> getAllStocksLiveData() {
		return stockRepository.getStocksLiveData();
	}

	public LiveData<Stock> getStockBySymbolLiveData(String symbol) {
		return stockRepository.getStockBySymbolLiveData(symbol);
	}

	public void save(Stock stock) {
		stockRepository.save(stock);
	}

	public void updatePrice(String symbol, int price) {
		stockRepository.updatePrice(symbol, price);
	}

	public void delete(Stock stock) {
		stockRepository.delete(stock);
	}

	public void addSymbols(String symbolsCsv) {
		String[] symbols = symbolsCsv.split(",");
		for (String symbol : symbols) {
			stockRepository.save(new Stock(symbol));
		}
	}

}