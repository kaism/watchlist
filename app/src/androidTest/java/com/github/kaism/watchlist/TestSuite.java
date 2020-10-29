package com.github.kaism.watchlist;

import com.github.kaism.watchlist.db.StockDaoTest;
import com.github.kaism.watchlist.db.StockRepositoryTest;
import com.github.kaism.watchlist.ui.AddStockActivityTest;
import com.github.kaism.watchlist.ui.MainActivityTest;
import com.github.kaism.watchlist.ui.StockListItemTest;
import com.github.kaism.watchlist.ui.StockListTest;
import com.github.kaism.watchlist.utils.UtilsPriceToStringTest;
import com.github.kaism.watchlist.utils.UtilsStringToPriceTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

		// unit tests (no screen needed)
		UtilsStringToPriceTest.class,
		UtilsPriceToStringTest.class,

		// db tests (no screen needed)
		StockDaoTest.class,
		StockRepositoryTest.class,

		// ui tests (screen required to be on)
		MainActivityTest.class,
		StockListTest.class,
		StockListItemTest.class,
		AddStockActivityTest.class

})
public class TestSuite {}