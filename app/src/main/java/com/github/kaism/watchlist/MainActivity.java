package com.github.kaism.watchlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.kaism.watchlist.api.ApiCalls;
import com.github.kaism.watchlist.api.Quote;
import com.github.kaism.watchlist.db.Stock;
import com.github.kaism.watchlist.ui.stocks.EditStockActivity;
import com.github.kaism.watchlist.ui.stocks.StockListAdapter;
import com.github.kaism.watchlist.ui.stocks.StockViewModel;
import com.github.kaism.watchlist.utils.AddStockDialogBuilder;
import com.github.kaism.watchlist.utils.ConfirmDeleteDialogBuilder;
import com.github.kaism.watchlist.utils.ListItemTouchHelper;
import com.github.kaism.watchlist.utils.RefreshListener;
import com.github.kaism.watchlist.utils.ScrollListener;
import com.github.kaism.watchlist.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;


public class MainActivity extends AppCompatActivity {
	private StockViewModel stockViewModel;
	public SwipeRefreshLayout swipeRefreshLayout;
	private String symbolsCsv = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// set up recycler view and scroll action
		final FloatingActionButton addStockButton = findViewById(R.id.button_add_stock);
		RecyclerView recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addOnScrollListener(new ScrollListener() {
			@Override
			public void onScrollUp() {
				addStockButton.setVisibility(View.GONE);
			}
			@Override
			public void onScrollDown() {
				addStockButton.setVisibility(View.VISIBLE);
			}
		});

		// set up adapter and item click action
		final StockListAdapter adapter = new StockListAdapter(this) {
			@Override
			public void onItemClicked(Stock stock) {
				Intent intent = new Intent(MainActivity.this, EditStockActivity.class);
				intent.putExtra(EditStockActivity.SYMBOL, stock.getSymbol());
				startActivity(intent);
			}
		};
		recyclerView.setAdapter(adapter);

		// set delete item action with swipe
		new ListItemTouchHelper(new ListItemTouchHelper.Callback() {
			@Override
			public void onSwipedLeft(int position) {
				final Stock stock = adapter.getStockAtPosition(position);
				new ConfirmDeleteDialogBuilder(stock, MainActivity.this) {
					@Override
					public void onConfirm() {
						stockViewModel.delete(stock);
					}
				}.show();
				adapter.notifyItemChanged(position);
			}
		}).attachToRecyclerView(recyclerView);

		// set up view model and observer
		final TextView emptyTextView = findViewById(R.id.empty_text);
		stockViewModel = new ViewModelProvider(this).get(StockViewModel.class);
		stockViewModel.getAllStocksLiveData().observe(this, new Observer<List<Stock>>() {
			@Override
			public void onChanged(List<Stock> stocks) {
				adapter.setStocks(stocks);
				symbolsCsv = Utils.getSymbolsCsv(stocks);
				if (stocks.size() > 0) {
					emptyTextView.setVisibility(View.GONE);
				} else {
					emptyTextView.setVisibility(View.VISIBLE);
				}
			}
		});

		// handle add stock fab click
		addStockButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new AddStockDialogBuilder(MainActivity.this) {
					@Override
					public void onAdd(String symbol) {
						if (symbol != null && !symbol.equals("")) {
							Intent intent = new Intent(MainActivity.this, EditStockActivity.class);
							intent.putExtra(EditStockActivity.SYMBOL, symbol);
							startActivity(intent);
						}
					}
				}.show();
			}
		});

		// handle add stock fab long click (quick seed)
		addStockButton.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				stockViewModel.addSymbols(BuildConfig.QUICKSEED_CSV);
				return true;
			}
		});

		// set up swipe to refresh
		swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
		swipeRefreshLayout.setOnRefreshListener(new RefreshListener(this) {
			@Override
			public void onRefreshDoInBackground() {
				ApiCalls.getQuotes(symbolsCsv).enqueue(new ApiCalls.QuotesCallback() {
					@Override
					public void onQuotesReceived(ArrayList<Quote> quotes) {
						for (Quote quote : quotes) {
							stockViewModel.updatePrice(quote.getSymbol(), Utils.stringToPrice(quote.getPrice()));
						}
					}
				});
			}
			@Override
			public void onRefreshPostExecute(boolean success) {
				swipeRefreshLayout.setRefreshing(false);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		setAutoRefreshStatus(menu.findItem(R.id.autoRefresh), Preferences.getPreferences(this).autoUpdate());
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
		super.onOptionsItemSelected(menuItem);
		if (menuItem.getItemId() == R.id.autoRefresh) {
			Preferences.setAutoUpdate(this, !Preferences.getPreferences(this).autoUpdate());
			setAutoRefreshStatus(menuItem, Preferences.getPreferences(this).autoUpdate());
		} else {
			return super.onOptionsItemSelected(menuItem);
		}
		return true;
	}

	private void setAutoRefreshStatus(MenuItem menuItem, boolean active) {
		if (menuItem.getItemId() == R.id.autoRefresh) {
			if (active) {
				menuItem.setIcon(R.drawable.ic_auto_refresh_on);
				menuItem.setTitle(R.string.menu_turn_auto_refresh_off);
				setTimerOn();
			} else {
				menuItem.setIcon(R.drawable.ic_auto_refresh_off);
				menuItem.setTitle(R.string.menu_turn_auto_refresh_on);
				setTimerOff();
			}
		}
	}


	private Timer autoRefreshTimer;
	private int timerDelay = (3*1000);

	private final TimerTask timerTask = new TimerTask() {
		@Override
		public void run() {
			if (!allPricesAreCurrent()) {
				int randomNum = ThreadLocalRandom.current().nextInt(7000, 10000 + 1);
				stockViewModel.updatePrice("AMD", randomNum);
			}
		}
	};

	private void setTimerOn() {
		if (autoRefreshTimer == null) {
			autoRefreshTimer = new Timer();
		}
		autoRefreshTimer.schedule(timerTask, 3000, timerDelay);
	}

	private void setTimerOff() {
		if (autoRefreshTimer != null) {
			autoRefreshTimer.cancel();
			autoRefreshTimer = null;
		}
	}

	private boolean allPricesAreCurrent() {
		return false;
	}

}