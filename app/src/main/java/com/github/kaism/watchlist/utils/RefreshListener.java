package com.github.kaism.watchlist.utils;

import android.os.AsyncTask;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.github.kaism.watchlist.MainActivity;
import java.lang.ref.WeakReference;


public class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
	private RefreshAsyncTask refreshAsyncTask;

	public RefreshListener(MainActivity activity) {
		this.refreshAsyncTask = new RefreshAsyncTask(activity);
	}

	/**
	 * Callback method to define what to do in background.
	 */
	public void onRefreshDoInBackground() {}

	/**
	 * Callback method to define what to do when background task is is finished.
	 */
	public void onRefreshPostExecute(boolean success) {}

	@Override
	public void onRefresh() {
		refreshAsyncTask.execute();
	}

	private class RefreshAsyncTask extends AsyncTask<Void, Integer, Boolean> {
		private WeakReference<MainActivity> activityReference;
		private RefreshAsyncTask(MainActivity activity) { activityReference = new WeakReference<>(activity); }
		private void refreshAsyncTaskDoInBackground() { onRefreshDoInBackground(); }
		private void refreshAsyncTaskOnPostExecute(boolean success) { onRefreshPostExecute(success); }

		@Override
		protected Boolean doInBackground(Void... voids) {
			MainActivity activity = activityReference.get();
			if (activity == null || activity.isFinishing()) return false;
			refreshAsyncTaskDoInBackground();
			return true;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			MainActivity activity = activityReference.get();
			if (activity == null || activity.isFinishing()) return;
			refreshAsyncTaskOnPostExecute(success);
		}
	}

}
