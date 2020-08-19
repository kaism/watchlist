package com.github.kaism.watchlist.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;


@Database(entities = {Stock.class}, version = 1, exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {
	private static RoomDatabase INSTANCE;

	public static RoomDatabase getDatabase(final Context context) {
		if (INSTANCE == null) {
			synchronized (RoomDatabase.class) {
				if (INSTANCE == null) {
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
							RoomDatabase.class, "kaism.watchlist.db")
							.build();
				}
			}
		}
		return INSTANCE;
	}

	public abstract StockDao getStockDao();

}
