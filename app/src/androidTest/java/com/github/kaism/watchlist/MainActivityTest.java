package com.github.kaism.watchlist;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityTest {

	@Rule
	public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

	@Test
	public void verifyDisplay() {
		// verify app name is displayed
		onView(withText(R.string.app_name)).check(matches(isDisplayed()));

		// verify get started text is displayed
		onView(withText(R.string.empty_text)).check(matches(isDisplayed()));

		//verify  button to add a stock is displayed
		onView(withId(R.id.button_add_stock)).check(matches(isDisplayed()));

		// verify clicking on add button opens add stock activity
		onView(withId(R.id.button_add_stock)).perform(click());
		onView(withText(R.string.add_stock_activity_title)).check(matches(isDisplayed()));
	}

}