package com.github.kaism.watchlist;


import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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
		onView(withText(R.string.add_stock)).check(matches(isDisplayed()));
	}

}