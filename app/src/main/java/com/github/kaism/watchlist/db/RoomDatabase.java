package com.github.kaism.watchlist.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Stock.class}, version = 2, exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {
	private static RoomDatabase INSTANCE;

	public static RoomDatabase getDatabase(final Context context) {
		if (INSTANCE == null) {
			synchronized (RoomDatabase.class) {
				if (INSTANCE == null) {
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
							RoomDatabase.class, "kaism.watchlist.db")
							.addMigrations(MIGRATION_1_2)
							.build();
				}
			}
		}
		return INSTANCE;
	}

	public abstract StockDao getStockDao();

	static final Migration MIGRATION_1_2 = new Migration(1, 2) {
		@Override
		public void migrate(@NonNull SupportSQLiteDatabase database) {
			database.execSQL("ALTER TABLE stocks ADD COLUMN currentPriceLastUpdated INTEGER NOT NULL DEFAULT 0");
		}
	};

}
