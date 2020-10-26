package com.github.kaism.watchlist.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class StockRepository {
	private StockDao stockDao;
	private LiveData<List<Stock>> allStocks;

	public StockRepository(Application application) {
		RoomDatabase db = RoomDatabase.getDatabase(application);
		stockDao = db.getStockDao();
		allStocks = stockDao.getAllStocks();
	}

	public LiveData<List<Stock>> getAllStocks() {
		return allStocks;
	}

	// insert
	public void insert(Stock stock) {
		new insertAsyncTask(stockDao).execute(stock);
	}

	private static class insertAsyncTask extends AsyncTask<Stock, Void, Void> {
		private StockDao asyncTaskDao;

		insertAsyncTask(StockDao dao) {
			asyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(final Stock... stocks) {
			asyncTaskDao.insert(stocks[0]);
			return null;
		}
	}

	// updatePrice
	public void updatePrice(String symbol, int price) {
		new updatePriceAsyncTask(stockDao, symbol, price).execute();
	}

	private static class updatePriceAsyncTask extends AsyncTask<String, Void, Void> {
		private StockDao asyncTaskDao;
		private String symbol;
		private int price;

		updatePriceAsyncTask(StockDao dao, String symbol, int price) {
			asyncTaskDao = dao;
			this.symbol = symbol;
			this.price = price;
		}

		@Override
		protected Void doInBackground(final String... params) {
			asyncTaskDao.updatePrice(this.symbol, this.price);
			return null;
		}
	}

	// delete
	public void delete(Stock stock) {
		new deleteAsyncTask(stockDao).execute(stock);
	}

	private static class deleteAsyncTask extends AsyncTask<Stock, Void, Void> {
		private StockDao asyncTaskDao;

		deleteAsyncTask(StockDao dao) {
			asyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(final Stock... stocks) {
			asyncTaskDao.delete(stocks[0]);
			return null;
		}
	}

}
