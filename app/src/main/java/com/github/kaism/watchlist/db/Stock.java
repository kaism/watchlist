package com.github.kaism.watchlist.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.github.kaism.watchlist.utils.Utils;

@Entity(tableName = "stocks")
public class Stock {

	@PrimaryKey @NonNull @ColumnInfo(name = "symbol") private String symbol;
	@ColumnInfo(name = "lowPrice") private int lowPrice;
	@ColumnInfo(name = "highPrice") private int highPrice;
	@ColumnInfo(name = "currentPrice") private int currentPrice;

	public Stock(@NonNull String symbol) { this.symbol = symbol.trim().toUpperCase(); }

	@Ignore
	public Stock(@NonNull String symbol, int lowPrice, int highPrice) {
		this.symbol = symbol;
		this.lowPrice = lowPrice;
		this.highPrice = highPrice;
	}

	@NonNull public String getSymbol() { return this.symbol; }
	public int getLowPrice() { return lowPrice; }
	public int getCurrentPrice() { return currentPrice; }
	public int getHighPrice() { return highPrice; }

	public void setLowPrice(int price) { lowPrice = price; }
	public void setHighPrice(int price) { highPrice = price; }
	public void setCurrentPrice(int price) { currentPrice = price; }

	public void setLowPrice(String price) { lowPrice = Utils.stringToPrice(price); }
	public void setHighPrice(String price) { highPrice = Utils.stringToPrice(price); }

}
