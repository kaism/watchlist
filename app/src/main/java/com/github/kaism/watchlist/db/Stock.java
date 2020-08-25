package com.github.kaism.watchlist.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "stocks")
public class Stock {

	@PrimaryKey(autoGenerate = true)
	protected int id;
	@NonNull
	@ColumnInfo(name = "symbol")
	private String symbol;

	public Stock(@NonNull String symbol) {
		this.symbol = symbol;
	}

	@NonNull
	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(@NonNull String symbol) {
		this.symbol = symbol;
	}

}
