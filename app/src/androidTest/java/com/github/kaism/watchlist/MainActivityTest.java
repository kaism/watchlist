package com.github.kaism.watchlist;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

	@Rule
	public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

	@Test
	public void app_opens() {
		onView(withText(R.string.app_name)).check(matches(isDisplayed()));
	}

	@Test
	public void get_started_instructions_displays_if_empty() {
		onView(withText(R.string.empty_text)).check(matches(isDisplayed()));
	}

	@Test
	public void button_to_add_is_displayed() {
		onView(withId(R.id.fab_add)).check(matches(isDisplayed()));
	}

	@Test
	public void button_opens_add_activity() {
		onView(withId(R.id.fab_add)).perform(click());
		onView(withText(R.string.add_stock_activity_title)).check(matches(isDisplayed()));
	}

}