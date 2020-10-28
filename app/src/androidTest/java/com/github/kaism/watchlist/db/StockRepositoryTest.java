package com.github.kaism.watchlist.db;

import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;

import com.github.kaism.watchlist.MainActivity;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

public class StockRepositoryTest {
	public StockRepository stockRepository;
	private Stock stock = new Stock("AMD");

	@Before
	public void getRepository() {
		ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
		scenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
			@Override
			public void perform(MainActivity activity) {
				stockRepository = new StockRepository(activity.getApplication());
			}
		});
		assertThat(stockRepository, not(equalTo(null)));
	}

	@Test
	public void canSaveStock() {
		Stock result;
		String symbol = stock.getSymbol();

		// insert
		stockRepository.save(stock);
		SystemClock.sleep(500); // give it time to do the async task

		// verify in database
		result = stockRepository.getStockBySymbol(symbol);
		assertThat(result, not(equalTo(null)));
	}

	@Test
	public void canDeleteStock() {
		Stock result;
		String symbol = stock.getSymbol();

		// insert
		stockRepository.save(stock);
		SystemClock.sleep(500); // give it time to do the async task

		// verify in database
		result = stockRepository.getStockBySymbol(symbol);
		assertThat(result, not(equalTo(null)));

		// delete
		stockRepository.delete(stock);
		SystemClock.sleep(500); // give it time to do the async task

		// verify not in database
		result = stockRepository.getStockBySymbol(symbol);
		assertThat(result, equalTo(null));
	}

}
