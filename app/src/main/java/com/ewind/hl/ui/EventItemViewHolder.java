package com.ewind.hl.ui;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.Score;
import com.ewind.hl.model.event.ScoreBand;

public abstract class EventItemViewHolder extends RecyclerView.ViewHolder {
    protected static final String TAG = LastEventItemViewHolder.class.getName();
    protected final EventActionListener listener;
    protected final TextView eventDateTextView;

    protected EventItem eventItem;

    public EventItemViewHolder(View itemView, EventActionListener listener) {
        super(itemView);
        this.listener = listener;
        this.eventDateTextView = itemView.findViewById(R.id.eventDateTextView);
    }

    public void setEvent(EventItem eventItem) {
        this.eventItem = eventItem;

        eventDateTextView.setText(eventItem.dateLabel);
    }

    protected int getBand(Event event) {
        Score score = event.getScore();
        return new ScoreBand(score).getBand();
    }

    public Event getEvent() {
        return eventItem.event;
    }

    public static final class EventItem {
        public final String dateLabel;
        public final Drawable icon;
        public final int tint;
        public final View detailsView;
        public final Event event;

        public EventItem(String dateLabel, Drawable icon, int tint, View detailsView, Event event) {
            this.dateLabel = dateLabel;
            this.icon = icon;
            this.tint = tint;
            this.detailsView = detailsView;
            this.event = event;
        }
    }
}
