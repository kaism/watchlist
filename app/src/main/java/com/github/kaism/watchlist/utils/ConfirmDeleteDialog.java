package com.github.kaism.watchlist.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.github.kaism.watchlist.R;
import com.github.kaism.watchlist.db.Stock;

public class ConfirmDeleteDialog extends AlertDialog.Builder {

	/**
	 * Callback method to be invoked when confirm clicked.
	 */
	public void onConfirm() {}

	public ConfirmDeleteDialog(Stock stock, Context context) {
		super(context);

		// build the question from symbol and translations for beginning and end of question
		setMessage(context.getString(R.string.dialog_question_confirm_delete_stock_begin) +
				stock.getSymbol() +
				context.getString(R.string.dialog_question_confirm_delete_stock_end));

		setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onConfirm();
			}
		});

		setNegativeButton(R.string.cancel, null);
	}
}
