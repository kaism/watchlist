package com.github.kaism.watchlist.ui.stocks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.github.kaism.watchlist.R;

public class AddStockDialogBuilder extends AlertDialog.Builder {

	/**
	 * Callback method to be invoked when add clicked.
	 */
	public void onAdd(String symbol) {}

	public AddStockDialogBuilder(Context context) {
		super(context);

		setView(R.layout.dialog_add_stock);
		setMessage(context.getString(R.string.dialog_message_enter_symbol));

		setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int which) {
				Dialog dialog = (Dialog) dialogInterface;
				EditText editText = dialog.findViewById(R.id.dialogAddStockSymbol);
				onAdd(editText.getText().toString());
			}
		});

		setNegativeButton(R.string.cancel, null);
	}

}
