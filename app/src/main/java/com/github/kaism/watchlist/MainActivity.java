package com.github.kaism.watchlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setOnClickListeners();
	}

	private void setOnClickListeners() {
		// add stock
		FloatingActionButton fab_add = findViewById(R.id.fab_add);
		fab_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getBaseContext(), AddStockActivity.class));
			}
		});
	}
}