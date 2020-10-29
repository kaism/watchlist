package com.github.kaism.watchlist.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ScrollListener extends RecyclerView.OnScrollListener {
	private String TAG = "KDBUG";

	/**
	 * Callback method to be invoked when RecyclerView has been scrolled up.
	 */
	public void onScrollUp() {}

	/**
	 * Callback method to be invoked when RecyclerView has been scrolled up.
	 */
	public void onScrollDown() {}


	@Override
	public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
		if (dy > 0) {
			Log.d(TAG, "onScrolled: scrolling up");
			onScrollUp();
		} else if (dy < 0) {
			Log.d(TAG, "onScrolled: scrolling down");
			onScrollDown();
		}
	}

	@Override
	public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
		boolean isScrolling = newState != RecyclerView.SCROLL_STATE_IDLE;
		if (isScrolling) Log.d(TAG, "onScrollStateChanged: scrolling");
		else Log.d(TAG, "onScrollStateChanged: idle");
	}

}
