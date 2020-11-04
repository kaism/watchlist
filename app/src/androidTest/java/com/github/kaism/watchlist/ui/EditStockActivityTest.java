package com.github.kaism.watchlist.ui;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.github.kaism.watchlist.MainActivity;
import com.github.kaism.watchlist.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class EditStockActivityTest {
	private String symbol = "AMD";
	private String low_price = "34.81";
	private String high_price = "119.93";

	@Rule
	public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

	@Before
	public void getHereFromMainActivity() {
		onView(withId(R.id.button_add_stock)).perform(click());
		onView(withId(R.id.dialogAddStockSymbol)).perform(typeText(symbol));
		onView(withText(R.string.add)).perform(click());
	}

	@Test
	public void verifyDisplay() {
		// verify activity title is displayed
		onView(withText(R.string.edit_stock_activity_title)).check(matches(isDisplayed()));

		// verify up arrow is displayed
		onView(withContentDescription(R.string.abc_action_bar_up_description)).check(matches(isDisplayed()));

		// verify form elements are displayed
		onView(withId(R.id.symbol)).check(matches(isDisplayed())).check(matches(withText(symbol)));
		onView(withId(R.id.lowPrice)).check(matches(isDisplayed()));
		onView(withId(R.id.highPrice)).check(matches(isDisplayed()));
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
	public void fillInForm() {
		// enter prices
		onView(withId(R.id.lowPrice)).check(matches(isDisplayed())).perform(typeText(low_price));
		onView(withId(R.id.highPrice)).check(matches(isDisplayed())).perform(typeText(high_price));

		// verify entered text
		onView(withText(low_price)).check(matches(isDisplayed()));
		onView(withText(high_price)).check(matches(isDisplayed()));

		// click save
		onView(withText(R.string.save)).check(matches(isDisplayed())).perform(click());

		// verify we are back at main activity
		onView(withText(R.string.app_name)).check(matches(isDisplayed()));

		// see stock in list
		onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
		onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText(symbol))));
	}

}