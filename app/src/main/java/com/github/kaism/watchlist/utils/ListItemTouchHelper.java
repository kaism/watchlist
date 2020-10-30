package com.github.kaism.watchlist.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ListItemTouchHelper extends ItemTouchHelper {

	public ListItemTouchHelper(@NonNull ListItemTouchHelper.Callback callback) {
		super(callback);
	}

	public static class Callback extends ItemTouchHelper.SimpleCallback {

		/**
		 * Callback method to be invoked when item has been swiped left.
		 */
		public void onSwipedLeft(int position) {}

		public Callback() {
			super(0, ItemTouchHelper.LEFT);
		}

		@Override
		public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
			return false;
		}

		@Override
		public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
			int position = viewHolder.getAdapterPosition();
			onSwipedLeft(position);
		}
	}

}
