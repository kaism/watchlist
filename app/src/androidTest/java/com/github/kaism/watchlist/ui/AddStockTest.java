package com.github.kaism.watchlist.ui;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.github.kaism.watchlist.MainActivity;
import com.github.kaism.watchlist.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class AddStockTest {
	String symbol = "AMD";

	@Rule
	public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

	@Before
	public void clickOnFab() {
		onView(withId(R.id.button_add_stock)).perform(click());
	}

	@Test
	public void addStockButtonOpensDialog() {
		// verify views are displayed
		onView(withText(R.string.dialog_message_enter_symbol)).check(matches(isDisplayed()));
		onView(withId(R.id.dialogAddStockSymbol)).check(matches(isDisplayed()));
		onView(withText(R.string.cancel)).check(matches(isDisplayed()));
		onView(withText(R.string.add)).check(matches(isDisplayed()));
	}

	@Test
	public void typeSymbolClickCancel() {
		// enter text
		onView(withId(R.id.dialogAddStockSymbol)).perform(typeText(symbol));

		// check buttons are still visible
		onView(withText(R.string.cancel)).check(matches(isCompletelyDisplayed()));
		onView(withText(R.string.add)).check(matches(isCompletelyDisplayed()));

		// click on cancel
		onView(withText(R.string.cancel)).perform(click());

		// verify dialog canceled
		onView(withText(R.string.edit_stock_activity_title)).check(doesNotExist());
		onView(withText(R.string.dialog_message_enter_symbol)).check(doesNotExist());
	}

	@Test
	public void typeSymbolClickAdd() {
		// enter text
		onView(withId(R.id.dialogAddStockSymbol)).perform(typeText(symbol));

		// check buttons are still visible
		onView(withText(R.string.cancel)).check(matches(isCompletelyDisplayed()));
		onView(withText(R.string.add)).check(matches(isCompletelyDisplayed()));

		// click on add
		onView(withText(R.string.add)).perform(click());

		// verify edit stock activity
		onView(withText(R.string.edit_stock_activity_title)).check(matches(isDisplayed()));
	}

}
