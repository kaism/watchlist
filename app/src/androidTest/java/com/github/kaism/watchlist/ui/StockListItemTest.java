package com.github.kaism.watchlist.ui;

import com.github.kaism.watchlist.R;
import com.github.kaism.watchlist.Utils;
import com.github.kaism.watchlist.db.Stock;
import com.github.kaism.watchlist.db.StockRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.github.kaism.watchlist.ViewMatchers.withBackgroundColor;
import static com.github.kaism.watchlist.ViewMatchers.withProgressPercent;

public class StockListItemTest {
	private StockRepository stockRepository;
	private List<Stock> backupStocks;

	private String symbol = "AMD";
	private int lowPriceInt = 3203;
	private String lowPriceString = "32.03";
	private int highPriceInt = 9428;
	private String highPriceString = "94.28";
	private Stock stock = new Stock(symbol, lowPriceInt, highPriceInt);

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
	public void itemUnknownPrice() {
		// insert stock
		stockRepository.insert(stock);

		// verify symbol
		onView(withId(R.id.symbol)).check(matches(isDisplayed())).check(matches(withText(symbol))).check(matches(withBackgroundColor(R.color.transparent)));

		// verify progress bar
		onView(withId(R.id.progressBar)).check(matches(isDisplayed())).check(matches(withProgressPercent(0)));

		// verify price text
		onView(withId(R.id.lowPriceText)).check(matches(isDisplayed())).check(matches(withText(lowPriceString)));
		onView(withId(R.id.highPriceText)).check(matches(isDisplayed())).check(matches(withText(highPriceString)));
		onView(withId(R.id.currentPriceText)).check(matches(isDisplayed())).check(matches(withText("0.00")));
	}

	@Test
	public void itemPriceInRange() {
		// insert stock
		stockRepository.insert(stock);

		// set current price to halfway
		int currentPriceInt = (lowPriceInt + highPriceInt) / 2;
		String currentPriceString = Utils.priceToString(currentPriceInt);
		stockRepository.updatePrice(stock.getSymbol(), currentPriceInt);

		// verify symbol
		onView(withId(R.id.symbol)).check(matches(isDisplayed())).check(matches(withText(symbol))).check(matches(withBackgroundColor(R.color.transparent)));

		// verify progress bar
		onView(withId(R.id.progressBar)).check(matches(isDisplayed())).check(matches(withProgressPercent(50)));

		// verify price text
		onView(withId(R.id.lowPriceText)).check(matches(isDisplayed())).check(matches(withText(lowPriceString)));
		onView(withId(R.id.highPriceText)).check(matches(isDisplayed())).check(matches(withText(highPriceString)));
		onView(withId(R.id.currentPriceText)).check(matches(isDisplayed())).check(matches(withText(currentPriceString)));
	}

	@Test
	public void itemPriceBelowRange() {
		stockRepository.insert(stock);

		int currentPriceInt = lowPriceInt - 1;
		String currentPriceString = Utils.priceToString(currentPriceInt);
		stockRepository.updatePrice(stock.getSymbol(), currentPriceInt);

		// verify symbol
		onView(withId(R.id.symbol)).check(matches(isDisplayed())).check(matches(withText(symbol))).check(matches(withBackgroundColor(R.color.red)));

		// verify progress bar
		onView(withId(R.id.progressBar)).check(matches(isDisplayed())).check(matches(withProgressPercent(0)));

		// verify price text
		onView(withId(R.id.lowPriceText)).check(matches(isDisplayed())).check(matches(withText(lowPriceString)));
		onView(withId(R.id.highPriceText)).check(matches(isDisplayed())).check(matches(withText(highPriceString)));
		onView(withId(R.id.currentPriceText)).check(matches(isDisplayed())).check(matches(withText(currentPriceString)));
	}

	@Test
	public void itemPriceAboveRange() {
		stockRepository.insert(stock);

		int currentPriceInt = highPriceInt + 1;
		String currentPriceString = Utils.priceToString(currentPriceInt);
		stockRepository.updatePrice(stock.getSymbol(), currentPriceInt);

		// verify symbol
		onView(withId(R.id.symbol)).check(matches(isDisplayed())).check(matches(withText(symbol))).check(matches(withBackgroundColor(R.color.green)));

		// verify progress bar
		onView(withId(R.id.progressBar)).check(matches(isDisplayed())).check(matches(withProgressPercent(100)));

		// verify price text
		onView(withId(R.id.lowPriceText)).check(matches(isDisplayed())).check(matches(withText(lowPriceString)));
		onView(withId(R.id.highPriceText)).check(matches(isDisplayed())).check(matches(withText(highPriceString)));
		onView(withId(R.id.currentPriceText)).check(matches(isDisplayed())).check(matches(withText(currentPriceString)));
	}

}
