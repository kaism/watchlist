package com.github.kaism.watchlist.utils;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.lang.ref.WeakReference;


public class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
	private RefreshAsyncTask refreshAsyncTask;

	public RefreshListener(Activity activity) {
		refreshAsyncTask = new RefreshAsyncTask(activity);
	}

	/**
	 * Callback method to define what to do in background when refresh is called.
	 */
	public void onRefreshDoInBackground() {}

	/**
	 * Callback method to define what to do when background task is is finished.
	 */
	public void onRefreshPostExecute(boolean success) {}

	@Override
	public void onRefresh() {
		if (refreshAsyncTask.getStatus() == AsyncTask.Status.PENDING) {
			refreshAsyncTask.execute();
		} else {
			refreshAsyncTask.cancel(false);
			onRefreshPostExecute(false);
		}
	}

	private class RefreshAsyncTask extends AsyncTask<Void, Integer, Boolean> {
		private WeakReference<Activity> activityReference;
		private RefreshAsyncTask(Activity activity) { activityReference = new WeakReference<>(activity); }

		@Override
		protected Boolean doInBackground(Void... voids) {
			Activity activity = activityReference.get();
			if (activity == null || activity.isFinishing()) return false;
			onRefreshDoInBackground();
			return true;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			Activity activity = activityReference.get();
			if (activity == null || activity.isFinishing()) return;
			onRefreshPostExecute(success);
			refreshAsyncTask = new RefreshAsyncTask(activity);
		}
	}

}
