package com.github.kaism.watchlist.suite;

import com.github.kaism.watchlist.UtilsPriceToStringTest;
import com.github.kaism.watchlist.UtilsStringToPriceTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs all unit tests.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
		UtilsPriceToStringTest.class,
		UtilsStringToPriceTest.class
})
public class UnitTestSuite {}