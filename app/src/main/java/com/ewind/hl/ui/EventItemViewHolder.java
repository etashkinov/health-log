package com.ewind.hl.ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventType;

import java.util.Locale;

public class EventItemViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = EventItemViewHolder.class.getName();
    private final EventActionListener listener;

    private Event event;
    private FrameLayout eventDetailContainer;

    public EventItemViewHolder(ViewGroup parent, EventActionListener listener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false));
        this.listener = listener;

        itemView.setOnLongClickListener(this::onUpdate);
        itemView.findViewById(R.id.addButton).setOnClickListener(this::onAddLike);
        itemView.findViewById(R.id.editButton).setOnClickListener(this::onUpdate);
        itemView.findViewById(R.id.deleteButton).setOnClickListener(this::onDelete);

        eventDetailContainer = itemView.findViewById(R.id.eventDetailContainer);
    }

    void setEvent(Event event) {
        this.event = event;

        addDetailView(event);

        TextView eventDateTextView = itemView.findViewById(R.id.eventDateTextView);
        eventDateTextView.setText(event.getDate().toString());
    }

    private void onDelete(View view) {
        listener.onDelete(event);
    }

    private boolean onUpdate(View view) {
        listener.onUpdate(event);
        return true;
    }

    private void onAddLike(View view) {
        listener.onAddLike(event);
    }

    private void addDetailView(Event event) {
        eventDetailContainer.removeAllViews();
        Context context = itemView.getContext();
        EventType type = event.getType();
        try {
            String name = "event_item_" + type.name().toLowerCase();
            int layout = context.getResources().getIdentifier(name, "layout", context.getPackageName());
            LayoutInflater.from(context).inflate(layout, eventDetailContainer);
            if (event.getType() == EventType.TEMPERATURE) {
                TextView eventScoreTextView = eventDetailContainer.findViewById(R.id.eventScoreTextView);
                eventScoreTextView.setText(String.format(Locale.getDefault(), "%.1f°", event.getDetail().getScore()));
            }
        } catch (Resources.NotFoundException e) {
            Log.w(TAG, "Detail view layout for " + type + " not found");
            addDefaultDetailView(event);
        }
    }

    private void addDefaultDetailView(Event event) {
        LayoutInflater.from(itemView.getContext()).inflate(R.layout.event_item_value, eventDetailContainer);
        TextView eventTypeTextView = eventDetailContainer.findViewById(R.id.eventTypeTextView);
        String name = LocalizationService.getEventTypeName(event.getType());
        double score = event.getDetail().getScore();
        eventTypeTextView.setText(String.format(Locale.getDefault(), "%s: %.2f", name, score));
    }
}
