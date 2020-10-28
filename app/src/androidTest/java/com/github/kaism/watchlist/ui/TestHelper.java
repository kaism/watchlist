package com.github.kaism.watchlist.ui;

import androidx.test.core.app.ActivityScenario;

import com.github.kaism.watchlist.MainActivity;
import com.github.kaism.watchlist.db.Stock;
import com.github.kaism.watchlist.db.StockRepository;

import java.util.List;

public class TestHelper {
	private static StockRepository stockRepository;

	public static StockRepository getRepository() {
		ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
		scenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
			@Override
			public void perform(MainActivity activity) {
				stockRepository = new StockRepository(activity.getApplication());
			}
		});
		return stockRepository;
	}

	public static void deleteStocks(StockRepository stockRepository, List<Stock> stocks) {
		if (stocks.size() > 0) {
			for (Stock stock : stocks) {
				stockRepository.delete(stock);
			}
		}
	}

	public static void addStocks(StockRepository stockRepository, List<Stock> stocks) {
		if (stocks.size() > 0) {
			for (Stock stock : stocks) {
				stockRepository.save(stock);
			}
		}

	}



}
