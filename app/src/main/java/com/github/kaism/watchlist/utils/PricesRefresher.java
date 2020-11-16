package com.github.kaism.watchlist.utils;

import android.app.Activity;
import android.util.Log;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.kaism.watchlist.MainActivity;
import com.github.kaism.watchlist.api.ApiCalls;
import com.github.kaism.watchlist.api.Quote;
import com.github.kaism.watchlist.ui.stocks.StockViewModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PricesRefresher {
	private Activity activity;
	private StockViewModel stockViewModel;
	private SwipeRefreshLayout swipeRefreshLayout;
	private Timer autoRefreshTimer;

	private int status = STATUS_READY;
	private String symbolsCsv = "";
	private final int cooldownTime = 5*1000;
	private int autoRefreshTimerDelay = (5*60*1000);

	// status codes
	private static final int STATUS_READY = 1;
	private static final int STATUS_BUSY = 2;
	private static final int STATUS_COOLDOWN = 3;

	public PricesRefresher(MainActivity mainActivity, StockViewModel stockViewModel, SwipeRefreshLayout swipeRefreshLayout) {
		this.activity = mainActivity;
		this.stockViewModel = stockViewModel;
		this.swipeRefreshLayout = swipeRefreshLayout;
		setRefreshListener();
	}

	public void setSymbolsCsv(String symbolsCsv) { this.symbolsCsv = symbolsCsv; }

	public void setAutoRefreshTimerOn() {
		setAutoRefreshTimerOff();
		autoRefreshTimer = new Timer();
		autoRefreshTimer.schedule(timerTask, cooldownTime, autoRefreshTimerDelay);
	}

	public void setAutoRefreshTimerOff() {
		if (autoRefreshTimer != null) { autoRefreshTimer.cancel(); }
	}

	private final TimerTask timerTask = new TimerTask() {
		@Override
		public void run() {
			if (status == STATUS_READY) {
				Log.d("KDBUG", "---------- autoRefreshTimer run BEGIN");
				refreshPrices();
			} else if (status == STATUS_BUSY) {
				Log.d("KDBUG", "autoRefreshTimer run status BUSY");
			} else if (status == STATUS_COOLDOWN) {
				Log.d("KDBUG", "outoRefreshTimer run status STILL IN COOLDOWN");
			}
		}
	};

	private void setRefreshListener() {
		swipeRefreshLayout.setOnRefreshListener(new RefreshListener((Activity) activity) {
			@Override
			public void onRefreshDoInBackground() {
				if (status == STATUS_READY) {
					swipeRefreshLayout.setRefreshing(true);
					Log.d("KDBUG", "---------- onRefreshDoInBackground BEGIN");
					Log.d("KDBUG", "onRefreshDoInBackground status READY");
					refreshPrices();
				} else if (status == STATUS_BUSY) {
					Log.d("KDBUG", "onRefreshDoInBackground status BUSY");
				} else if (status == STATUS_COOLDOWN) {
					Log.d("KDBUG", "onRefreshDoInBackground status STILL IN COOLDOWN");
					swipeRefreshLayout.setRefreshing(false);
				}
			}
			@Override
			public void onRefreshPostExecute(boolean success) {
				if (success) {
					Log.d("KDBUG", "onRefreshPostExecute refresh SUCCESS, starting cooldown");
					startCooldown();
				} else {
					Log.d("KDBUG", "onRefreshPostExecute refresh NO SUCCESS, quitting");
				}
				swipeRefreshLayout.setRefreshing(false);
			}
		});
	}

	// start timer to reset status after a cooldown period
	private void startCooldown() {
		if (status == STATUS_BUSY) {
			Log.d("KDBUG", "startCooldown cooldown START");
			status = STATUS_COOLDOWN;
			new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						status = STATUS_READY;
						Log.d("KDBUG", "cooldownTimerTask cooldown END");
						Log.d("KDBUG", "onRefreshPostExecute END ----------");
					}
				}, cooldownTime
			);
		} else if (status == STATUS_COOLDOWN) {
			Log.d("KDBUG", "ERROR: startCooldown status STILL IN COOLDOWN");
		}
	}

	private void refreshPrices() {
		Log.d("KDBUG", "refreshPrices REFRESHING prices");
		status = STATUS_BUSY;
		if (!symbolsCsv.equals("")) {
			ApiCalls.getQuotes(symbolsCsv).enqueue(new ApiCalls.QuotesCallback() {
				@Override
				public void onQuotesReceived(ArrayList<Quote> quotes) {
					for (Quote quote : quotes) {
						stockViewModel.updatePrice(quote.getSymbol(), Utils.stringToPrice(quote.getPrice()));
					}
				}
			});
		}
	}




}
