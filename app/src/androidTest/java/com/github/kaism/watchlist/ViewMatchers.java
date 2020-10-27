package com.github.kaism.watchlist;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ProgressBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


public final class ViewMatchers {

	private ViewMatchers() {}

	/**
	 * Returns a Matcher that matches Views based on background color.
	 *
	 * @param resourceId the resource id of the color to match.
	 */
	public static Matcher<View> withBackgroundColor(final int resourceId) {
		return new WithBackgroundColorFromIdMatcher(resourceId);
	}

	/**
	 * Returns a Matcher that matches ProgressBar Views based on progress percent between
	 * min and max range, where 0 is min and 100 is max.
	 *
	 * @param progress the progress percent to match, a number between 0-100.
	 */
	public static Matcher<View> withProgressPercent(final int progress) {
		return new WithProgressPercentMatcher(progress);
	}

	static final class WithBackgroundColorFromIdMatcher extends TypeSafeMatcher<View> {
		private final int resourceId;

		private String resourceName = null;
		private String expectedText = null;
		private int color;

		private WithBackgroundColorFromIdMatcher(final int resourceId) {
			this.resourceId = resourceId;
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("with background color from resource id: ");
			description.appendValue(resourceId);
			if (null != this.resourceName) {
				description.appendText("[");
				description.appendText(resourceName);
				description.appendText("]");
			}
			if (null != this.expectedText) {
				description.appendText(" value: ");
				description.appendText(expectedText);
			}
		}

		@Override
		public boolean matchesSafely(View view) {
			if (null == this.expectedText) {
				try {
					expectedText = view.getResources().getString(resourceId);
					resourceName = view.getResources().getResourceEntryName(resourceId);
					color = view.getResources().getColor(resourceId);
				} catch (Resources.NotFoundException ignored) {
					// view could be from a context unaware of the resource id.
				}
			}
			if (null != expectedText) {
				return color == ((ColorDrawable) view.getBackground()).getColor();
			} else {
				return false;
			}
		}
	}

	static final class WithProgressPercentMatcher extends TypeSafeMatcher<View> {
		private final int percent;

		private int actualPercent;
		private String rangeText;

		private WithProgressPercentMatcher(final int percent) {
			this.percent = percent;
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("progress percent ");
			description.appendValue(percent);

			if (null != this.rangeText) {
				description.appendText(" Got: ");
				description.appendValue(actualPercent);
				description.appendText(rangeText);
			}
		}

		@Override
		protected boolean matchesSafely(View item) {
			ProgressBar progressBar = (ProgressBar) item;
			int max = progressBar.getMax();
			int actualValue = progressBar.getProgress();
			actualPercent = (int) Math.round((actualValue*1.0/max)*100);

			rangeText = " (value " + actualValue + " in range " + progressBar.getMin() + " - " + max + ")";

			return actualPercent == percent;
		}
	}

}



