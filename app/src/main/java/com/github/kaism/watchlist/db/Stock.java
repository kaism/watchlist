package com.github.kaism.watchlist.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "stocks")
public class Stock {

	@PrimaryKey
	@NonNull
	@ColumnInfo(name = "symbol")
	private String symbol;

	@ColumnInfo(name = "lowPrice")
	private String lowPrice;

	@ColumnInfo(name = "highPrice")
	private String highPrice;

	public Stock(@NonNull String symbol) {
		this.symbol = symbol;
	}

	@NonNull
	public String getSymbol() {
		return this.symbol;
	}

	public String getLowPrice() {
		return this.lowPrice;
	}

	public String getHighPrice() {
		return this.highPrice;
	}

	public void setLowPrice(String price) {
		this.lowPrice = price;
	}

	public void setHighPrice(String price) {
		this.highPrice = price;
	}
}
