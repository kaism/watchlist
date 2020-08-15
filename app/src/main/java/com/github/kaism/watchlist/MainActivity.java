package com.github.kaism.watchlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
	private static final int NEW_STOCK_ACTIVITY_REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setOnClickListeners();
	}

	private void setOnClickListeners() {
		// add stock
		findViewById(R.id.button_add_stock).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivityForResult(
						new Intent(MainActivity.this, AddStockActivity.class),
						NEW_STOCK_ACTIVITY_REQUEST_CODE
				);
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == NEW_STOCK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
			Toast.makeText(getApplicationContext(), "Returned OK", Toast.LENGTH_LONG).show();
		}
	}
}