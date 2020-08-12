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
	public void fillInAddForm() {
		String ticker = "AMD";
		String low_price = "34.81";
		String high_price = "119.93";

		// verify activity title is displayed
		onView(withText(R.string.add_stock_activity_title)).check(matches(isDisplayed()));

		// enter a ticker
		onView(withId(R.id.ticker)).check(matches(isDisplayed())).perform(typeText(ticker));

		// enter a low price
		onView(withId(R.id.low_price)).check(matches(isDisplayed())).perform(typeText(low_price));

		// enter a high price
		onView(withId(R.id.high_price)).check(matches(isDisplayed())).perform(typeText(high_price));

		// verify entered text
		onView(withText(ticker)).check(matches(isDisplayed()));
		onView(withText(low_price)).check(matches(isDisplayed()));
		onView(withText(high_price)).check(matches(isDisplayed()));

		// click save
		onView(withText(R.string.save)).check(matches(isDisplayed())).perform(click());
	}

}