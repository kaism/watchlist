package com.github.kaism.watchlist.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class StockRepository {
	private StockDao stockDao;

	public StockRepository(Application application) {
		RoomDatabase db = RoomDatabase.getDatabase(application);
		stockDao = db.getStockDao();
	}

	// get all stocks
	public LiveData<List<Stock>> getStocksLiveData() { return stockDao.getStocksLiveData(); }
	public List<Stock> getStocks() { return stockDao.getStocks(); }

	// get stock
	public LiveData<Stock> getStockBySymbolLiveData(String symbol) { return stockDao.getStockBySymbolLiveData(symbol); }
	public Stock getStockBySymbol(String symbol) { return stockDao.getStockBySymbol(symbol); }

	// save
	public void save(Stock stock) {
		new insertAsyncTask(stockDao).execute(stock);
	}
	private static class insertAsyncTask extends AsyncTask<Stock, Void, Void> {
		private StockDao asyncTaskDao;

		insertAsyncTask(StockDao dao) {
			asyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(Stock... stocks) {
			asyncTaskDao.insert(stocks[0]);
			return null;
		}
	}

	public void updatePrice(String symbol, int price) {
		new updatePriceAsyncTask(stockDao, symbol, price).execute();
	}

	// update price
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
			asyncTaskDao.updatePrice(symbol, price);
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
