package com.github.kaism.watchlist;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class MainActivityTest {
	private String symbol = "AMD";
	private String low_price = "34.81";
	private String high_price = "119.93";

	@Rule
	public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

	@Test
	public void verifyDisplay() {
		// verify app name is displayed
		onView(withText(R.string.app_name)).check(matches(isDisplayed()));

		// verify button to add a stock is displayed
		onView(withId(R.id.button_add_stock)).check(matches(isDisplayed()));

		// verify clicking on add button opens add stock activity
		onView(withId(R.id.button_add_stock)).perform(click());
		onView(withText(R.string.add_stock_activity_title)).check(matches(isDisplayed()));
	}

	@Test
	public void getStartedText() {
		// when list is empty, verify is displayed
		onView(withText(R.string.empty_text)).check(matches(isDisplayed()));

		// when list contains at least one stock, verify is not displayed
		addStock();
		onView(withText(R.string.empty_text)).check(matches(not(isDisplayed())));
	}

	@Test
	public void listItem() {
		addStock();
		onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText(symbol))));
		onView(withText(symbol)).check(matches(hasSibling(withText(low_price))));
		onView(withText(symbol)).check(matches(hasSibling(withText(high_price))));
	}

	private void addStock() {
		onView(withId(R.id.button_add_stock)).perform(click());
		onView(withId(R.id.symbol)).perform(typeText(symbol));
		onView(withId(R.id.lowPrice)).perform(typeText(low_price));
		onView(withId(R.id.highPrice)).perform(typeText(high_price));
		onView(withText(R.string.save)).perform(click());
	}

}