package com.github.kaism.watchlist.suite;

import com.github.kaism.watchlist.AddStockActivityTest;
import com.github.kaism.watchlist.MainActivityTest;
import com.github.kaism.watchlist.StockDaoTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs all instrumentation tests.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
		AddStockActivityTest.class,
		MainActivityTest.class,
		StockDaoTest.class
})
public class InstrumentationTestSuite {}
