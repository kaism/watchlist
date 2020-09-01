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
	@Query("SELECT * from stocks ORDER BY symbol ASC")
	LiveData<List<Stock>> getAllStocks();

	@Query("SELECT * from stocks limit 1")
	List<Stock> selectOne();

	@Query("SELECT * from stocks where symbol=:symbol")
	List<Stock> selectBySymbol(String symbol);

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insert(Stock stock);

	@Query("UPDATE stocks SET currentPrice = :price WHERE symbol = :symbol")
	void updatePrice(String symbol, int price);

	@Delete
	void delete(Stock stock);
}