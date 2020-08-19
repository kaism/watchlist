package com.github.kaism.watchlist;


import androidx.test.espresso.Espresso;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddStockActivityTest {
	private String symbol = "AMD";
	private String low_price = "34.81";
	private String high_price = "119.93";

	@Rule
	public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

	//TODO: set up before/after to create a clean db and destroy it afterward - as it is, this test can only be run once

	@Before
	public void getHereFromMainActivity() {
		onView(withId(R.id.button_add_stock)).perform(click());
	}

	@Test
	public void verifyDisplay() {
		// verify activity title is displayed
		onView(withText(R.string.add_stock_activity_title)).check(matches(isDisplayed()));

		// verify up arrow is displayed
		onView(withContentDescription(R.string.abc_action_bar_up_description)).check(matches(isDisplayed()));

		// verify form elements are displayed
		onView(withId(R.id.symbol)).check(matches(isDisplayed()));
		onView(withId(R.id.low_price)).check(matches(isDisplayed()));
		onView(withId(R.id.high_price)).check(matches(isDisplayed()));
		onView(withText(R.string.save)).check(matches(isDisplayed()));
	}

	@Test
	public void clickUp() {
		// up goes to Main Activity
		onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
		onView(withText(R.string.app_name)).check(matches(isDisplayed()));
	}

	@Test
	public void clickBack() {
		// back goes to Main Activity
		Espresso.pressBack();
		onView(withText(R.string.app_name)).check(matches(isDisplayed()));
	}

	@Test
	public void fillInAddStockForm() {
		// verify activity title is displayed
		onView(withText(R.string.add_stock_activity_title)).check(matches(isDisplayed()));

		// enter a symbol
		onView(withId(R.id.symbol)).check(matches(isDisplayed())).perform(typeText(symbol));

		// enter a low price
		onView(withId(R.id.low_price)).check(matches(isDisplayed())).perform(typeText(low_price));

		// enter a high price
		onView(withId(R.id.high_price)).check(matches(isDisplayed())).perform(typeText(high_price));

		// verify entered text
		onView(withText(symbol)).check(matches(isDisplayed()));
		onView(withText(low_price)).check(matches(isDisplayed()));
		onView(withText(high_price)).check(matches(isDisplayed()));

		// click save
		onView(withText(R.string.save)).check(matches(isDisplayed())).perform(click());
	}

	@Test
	public void addStock() {
		// fill in form
		onView(withId(R.id.symbol)).perform(typeText(symbol));
		onView(withId(R.id.low_price)).perform(typeText(low_price));
		onView(withId(R.id.high_price)).perform(typeText(high_price));

		// click save
		onView(withText(R.string.save)).perform(click());

		// verify we are back at main activity
		onView(withText(R.string.app_name)).check(matches(isDisplayed()));

		// see stock in list
		onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
		onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText(symbol))));
	}

}