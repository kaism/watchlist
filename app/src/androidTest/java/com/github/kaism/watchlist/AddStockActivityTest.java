package com.github.kaism.watchlist;


import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddStockActivityTest {

	@Rule
	public ActivityTestRule<AddStockActivity> activityRule = new ActivityTestRule<>(AddStockActivity.class);

	@Test
	public void activity_opens() {
		onView(withText(R.string.add_stock_activity_title)).check(matches(isDisplayed()));
	}

	@Test
	public void can_enter_a_ticker() {
		String ticker = "AAPL";
		onView(withId(R.id.ticker)).check(matches(isDisplayed()))
				.perform(typeText(ticker));
		onView(withText(ticker)).check(matches(isDisplayed()));
	}

	@Test
	public void can_enter_a_low_price() {
		String price = "0.99";
		onView(withId(R.id.low_price)).check(matches(isDisplayed()))
				.perform(typeText(price));
		onView(withText(price)).check(matches(isDisplayed()));
	}

	@Test
	public void can_enter_a_high_price() {
		String price = "100";
		onView(withId(R.id.high_price)).check(matches(isDisplayed()))
				.perform(typeText(price));
		onView(withText(price)).check(matches(isDisplayed()));
	}

	@Test
	public void can_click_to_save() {
		onView(withText(R.string.save)).check(matches(isDisplayed()))
				.perform(click());
	}

}