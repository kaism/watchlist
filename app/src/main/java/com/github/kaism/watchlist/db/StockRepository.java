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
		protected Void doInBackground(final Stock... params) {
			asyncTaskDao.insert(params[0]);
			return null;
		}
	}

	// update
	public void update(Stock stock) {
		new updateAsyncTask(stockDao).execute(stock);
	}

	private static class updateAsyncTask extends AsyncTask<Stock, Void, Void> {
		private StockDao asyncTaskDao;

		updateAsyncTask(StockDao dao) {
			asyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(final Stock... params) {
			asyncTaskDao.update(params[0]);
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
		protected Void doInBackground(final Stock... params) {
			asyncTaskDao.delete(params[0]);
			return null;
		}
	}

}
