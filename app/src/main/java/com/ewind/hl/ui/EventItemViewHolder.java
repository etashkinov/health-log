package com.ewind.hl.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.Score;
import com.ewind.hl.model.event.ScoreBand;

import org.joda.time.LocalDateTime;

public abstract class EventItemViewHolder extends RecyclerView.ViewHolder {
    protected static final String TAG = LastEventItemViewHolder.class.getName();
    protected final EventActionListener listener;
    protected final TextView eventDateTextView;
    protected Event event;

    public EventItemViewHolder(View itemView, EventActionListener listener) {
        super(itemView);
        this.listener = listener;
        this.eventDateTextView = itemView.findViewById(R.id.eventDateTextView);
    }

    public void setEvent(Event event) {
        this.event = event;

        eventDateTextView.setText(LocalizationService.getEventDateFrom(event.getDate(), LocalDateTime.now()));
    }

    protected int getBand(Event event) {
        Score score = event.getScore();
        return new ScoreBand(score).getBand();
    }

    public Event getEvent() {
        return event;
    }
}
