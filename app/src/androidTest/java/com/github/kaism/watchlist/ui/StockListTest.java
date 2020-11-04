package com.github.kaism.watchlist.ui;

import com.github.kaism.watchlist.R;
import com.github.kaism.watchlist.db.Stock;
import com.github.kaism.watchlist.db.StockRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

public class StockListTest {
	private StockRepository stockRepository;
	private List<Stock> backupStocks;

	@Before
	public void setUp() {
		stockRepository = TestHelper.getRepository();
		backupStocks = stockRepository.getStocks();
		TestHelper.deleteStocks(stockRepository, backupStocks);
	}

	@After
	public void restoreDatabase() {
		TestHelper.deleteStocks(stockRepository, stockRepository.getStocks());
		TestHelper.addStocks(stockRepository, backupStocks);
	}

	@Test
	public void verifyEmptyDisplay() {
		// verify empty text is displayed
		onView(withId(R.id.empty_text)).check(matches(isDisplayed()));

		// verify button to add a stock is displayed
		onView(withId(R.id.button_add_stock)).check(matches(isDisplayed()));
	}

	@Test
	public void verifyOneStockDisplay() {
		// add stock
		stockRepository.save(new Stock("AMD"));

		// verify empty text not displayed
		onView(withId(R.id.empty_text)).check(matches(not(isDisplayed())));

		// verify button to add a stock is displayed
		onView(withId(R.id.button_add_stock)).check(matches(isDisplayed()));
	}

	@Test
	public void verifyMultipleStockDisplay() {
		// add stocks
		String[] symbols = {
				"AAPL", "AMD", "AMXN", "ATVI", "BABA", "CRM", "EA", "FB", "GOOG", "INTC", "MSFT", "MTCH",
				"NVDA", "PYPL", "TIVO", "TSLA", "TTD", "YELP", "ZG"
		};
		for (String symbol : symbols) {
			stockRepository.save(new Stock(symbol));
		}

		// verify empty text not displayed
		onView(withId(R.id.empty_text)).check(matches(not(isDisplayed())));

		// verify button to add a stock is displayed
		onView(withId(R.id.button_add_stock)).check(matches(isDisplayed()));

		// scroll up
		onView(withId(R.id.recycler_view)).perform(swipeUp());

		// verify button to add a stock is not displayed
		onView(withId(R.id.button_add_stock)).check(matches(not(isDisplayed())));

		// scroll down
		onView(withId(R.id.recycler_view)).perform(swipeDown());

		// verify button to add a stock is displayed
		onView(withId(R.id.button_add_stock)).check(matches(isDisplayed()));
	}

}
