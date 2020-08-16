package com.github.kaism.watchlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddStockActivity extends AppCompatActivity {
    // bundle key constants
    public static final String SYMBOL = "com.github.kaism.watchlist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        // handle save stock
        findViewById(R.id.save_stock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get value
                EditText symbolEditText = findViewById(R.id.symbol);
                String symbol = symbolEditText.getText().toString().toUpperCase();

                // return data to main activity
                Intent replyIntent = new Intent();
                replyIntent.putExtra(SYMBOL, symbol);
                setResult(RESULT_OK, replyIntent);

                finish();
            }
        });
    }
}