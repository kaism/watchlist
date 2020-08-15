package com.github.kaism.watchlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                // get value
                // return data to main activity
                Intent replyIntent = new Intent();
                setResult(RESULT_OK, replyIntent);

                finish();
            }
        });
    }
}