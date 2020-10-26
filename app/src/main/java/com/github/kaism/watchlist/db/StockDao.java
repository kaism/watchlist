package com.github.kaism.watchlist.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StockDao {

	@Query("SELECT * FROM stocks ORDER BY symbol ASC")
	LiveData<List<Stock>> getStocksLiveData();

	@Query("SELECT * FROM stocks where symbol=:symbol")
	LiveData<Stock> getStockBySymbolLiveData(String symbol);

	@Query("SELECT * FROM stocks ORDER BY symbol ASC")
	List<Stock> getStocks();

	@Query("SELECT * FROM stocks where symbol=:symbol")
	Stock getStockBySymbol(String symbol);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Stock stock);

	@Query("UPDATE stocks SET currentPrice = :price WHERE symbol = :symbol")
	void updatePrice(String symbol, int price);

	@Delete
	void delete(Stock stock);


	// for testing
	@Query("SELECT * FROM stocks where symbol=:symbol")
	List<Stock> selectBySymbol(String symbol);

}