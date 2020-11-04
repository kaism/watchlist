package com.github.kaism.watchlist;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


public class Preferences {
	private final static String SHAREDPREFS_FILENAME = "com.github.kaism.watchlist.prefs";
	private final static String SHAREDPREFS_AUTO_UPDATE_KEY = "auto_update";

	public static SharedPrefs getPreferences(Context context) {
		return new SharedPrefs(context);
	}

	public static void setAutoUpdate(Context context, boolean autoUpdate) {
		SharedPrefs sharedPrefs = new SharedPrefs(context);
		sharedPrefs.setAutoUpdate(autoUpdate);
	}

	public static class SharedPrefs {
		private final SharedPreferences preferences;
		private final boolean autoUpdate;

		SharedPrefs(Context context) {
			preferences = context.getSharedPreferences(SHAREDPREFS_FILENAME, MODE_PRIVATE);
			autoUpdate = preferences.getBoolean(SHAREDPREFS_AUTO_UPDATE_KEY, false);
		}

		public boolean autoUpdate() { return autoUpdate; }

		public void setAutoUpdate(boolean autoUpdate) {
			SharedPreferences.Editor preferencesEditor = preferences.edit();
			preferencesEditor.putBoolean(SHAREDPREFS_AUTO_UPDATE_KEY, autoUpdate).apply();
		}
	}

}

