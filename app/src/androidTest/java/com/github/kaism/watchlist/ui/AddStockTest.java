package com.github.kaism.watchlist.ui;

import android.os.SystemClock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.github.kaism.watchlist.MainActivity;
import com.github.kaism.watchlist.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class AddStockTest {
	String symbol = "AMD";

	@Rule
	public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

	@Test
	public void addStockDialog() {
		// click on add fab
		onView(withId(R.id.button_add_stock)).perform(click());

		// verify views are displayed
		onView(withText(R.string.dialog_message_enter_symbol)).check(matches(isDisplayed()));
		onView(withId(R.id.dialogAddStockSymbol)).check(matches(isDisplayed()));
		onView(withText(R.string.cancel)).check(matches(isDisplayed()));
		onView(withText(R.string.add)).check(matches(isDisplayed()));

		// enter text
		onView(withId(R.id.dialogAddStockSymbol)).perform(typeText(symbol));

		// check buttons are still visible
		onView(withText(R.string.cancel)).check(matches(isCompletelyDisplayed()));
		onView(withText(R.string.add)).check(matches(isCompletelyDisplayed()));

		// click on add
		onView(withText(R.string.add)).perform(click());

		// verify clicking on add button opens add stock activity
		onView(withText(R.string.edit_stock_activity_title)).check(matches(isDisplayed()));

		SystemClock.sleep(3000);
	}

}
