package com.github.kaism.watchlist;


import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.github.kaism.watchlist.db.RoomDatabase;
import com.github.kaism.watchlist.db.Stock;
import com.github.kaism.watchlist.db.StockDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Stock DAO integration test
 * <p>
 * Tests StockDao and its dependencies: RoomDatabase, & Stock (entity)
 */
@RunWith(AndroidJUnit4.class)
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
		stock = stockDao.selectOne().get(0);
		assertThat(stock.getSymbol(), equalTo(symbol));
	}

	@Test
	public void updateStock() {
		// insert stock
		String symbol_incorrect = "APL";
		Stock stock = new Stock(symbol_incorrect);
		stockDao.insert(stock);

		// update symbol
		stock = stockDao.selectOne().get(0);
		stock.setSymbol(symbol);
		stockDao.update(stock);

		// verify updated
		stock = stockDao.selectOne().get(0);
		assertThat(stock.getSymbol(), equalTo(symbol));
	}

	@Test
	public void deleteStock() {
		// insert
		Stock stock = new Stock(symbol);
		stockDao.insert(stock);

		// delete
		stock = stockDao.selectOne().get(0);
		stockDao.delete(stock);

		// verify not in db
		List<Stock> empty = stockDao.selectOne();
		assertThat(empty.size(), equalTo(0));
	}

}