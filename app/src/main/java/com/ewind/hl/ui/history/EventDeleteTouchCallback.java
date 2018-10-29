package com.ewind.hl.ui.history;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.ewind.hl.ui.EventActionListener;
import com.ewind.hl.ui.EventItemViewHolder;

import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;

public class EventDeleteTouchCallback extends ItemTouchHelper.SimpleCallback {

    private final EventActionListener listener;

    public static void attach(RecyclerView eventsList, Activity activity) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new EventDeleteTouchCallback(activity));
        itemTouchHelper.attachToRecyclerView(eventsList);
    }

    public EventDeleteTouchCallback(Activity activity) {
        super(0, LEFT);
        this.listener = new EventActionListener(activity);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
        if (swipeDir == LEFT) {
            listener.onDelete(((EventItemViewHolder)viewHolder).getEvent());
        }
    }
}
