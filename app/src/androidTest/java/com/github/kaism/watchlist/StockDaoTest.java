package com.github.kaism.watchlist;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.github.kaism.watchlist.db.RoomDatabase;
import com.github.kaism.watchlist.db.Stock;
import com.github.kaism.watchlist.db.StockDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class StockDaoTest {
	private StockDao stockDao;
	private RoomDatabase db;

	private String symbol = "AAPL";

	@Before
	public void createDb() {
		Context context = ApplicationProvider.getApplicationContext();
		db = Room.inMemoryDatabaseBuilder(context, RoomDatabase.class).build();
		stockDao = db.getStockDao();
	}

	@After
	public void closeDb() {
		db.close();
	}

	@Test
	public void insertStock() {
		// insert stock
		Stock stock = new Stock(symbol);
		stockDao.insert(stock);

		// verify in db
		stock = stockDao.getStockBySymbol(symbol);
		assertThat(stock.getSymbol(), equalTo(symbol));
	}

	@Test
	public void deleteStock() {
		// insert
		Stock stock = new Stock(symbol);
		stockDao.insert(stock);

		// delete
		stock = stockDao.getStockBySymbol(symbol);
		stockDao.delete(stock);

		// verify not in db
		Stock empty = stockDao.getStockBySymbol(symbol);
		assertThat(empty, equalTo(null));
	}

	@Test
	public void insertNoDuplicates() {
		// insert stock
		Stock stock = new Stock(symbol);
		stockDao.insert(stock);

		// insert duplicate stock
		stock = new Stock(symbol);
		stockDao.insert(stock);

		// verify only one
		List<Stock> stocks = stockDao.selectBySymbol(symbol);
		assertThat(stocks.size(), equalTo(1));
	}

}