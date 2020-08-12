package com.github.kaism.watchlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class AddStockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        // save stock
        findViewById(R.id.save_stock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save
                // go back to main activity
            }
        });
    }
}