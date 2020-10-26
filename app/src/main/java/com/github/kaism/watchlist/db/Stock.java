package com.github.kaism.watchlist.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "stocks")
public class Stock {

	@PrimaryKey @NonNull @ColumnInfo(name = "symbol") private String symbol;
	@ColumnInfo(name = "lowPrice") private int lowPrice;
	@ColumnInfo(name = "highPrice") private int highPrice;
	@ColumnInfo(name = "currentPrice") private int currentPrice;

	public Stock(@NonNull String symbol) {
		this.symbol = symbol;
	}

	@NonNull public String getSymbol() {
		return this.symbol;
	}
	public int getLowPrice() {
		return this.lowPrice;
	}
	public int getCurrentPrice() {
		return this.currentPrice;
	}
	public int getHighPrice() {
		return this.highPrice;
	}

	public void setLowPrice(int price) {
		this.lowPrice = price;
	}
	public void setHighPrice(int price) {
		this.highPrice = price;
	}
	public void setCurrentPrice(int price) {
		this.currentPrice = price;
	}

}
