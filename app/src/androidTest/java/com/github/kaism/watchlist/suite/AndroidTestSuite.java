package com.github.kaism.watchlist.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs all tests, unit & instrumentation.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({UnitTestSuite.class, InstrumentationTestSuite.class})
public class AndroidTestSuite {}
