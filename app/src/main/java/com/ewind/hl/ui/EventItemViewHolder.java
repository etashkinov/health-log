package com.ewind.hl.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.EventTypeFactory;

import org.joda.time.LocalDateTime;

public class EventItemViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = EventItemViewHolder.class.getName();
    private final EventActionListener listener;
    private final View addButton;

    private Event event;
    private final TextView eventDateTextView;

    public EventItemViewHolder(ViewGroup parent, EventActionListener listener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false));
        this.listener = listener;

        itemView.setOnLongClickListener(this::onUpdate);
//        itemView.findViewById(R.id.historyButton).setOnClickListener(this::onHistory);
        addButton = itemView.findViewById(R.id.addButton);
        addButton.setOnClickListener(this::onAddLike);

        eventDateTextView = itemView.findViewById(R.id.eventDateTextView);
    }

    void setEvent(Event event) {
        this.event = event;

        addDetailView(event);
        eventDateTextView.setText(LocalizationService.getEventDateFrom(event.getDate(), LocalDateTime.now()));

        if (!this.event.isExpired()) {
            addButton.setVisibility(View.INVISIBLE);
        }
    }

    private boolean onUpdate(View view) {
        listener.onUpdate(event);
        return true;
    }

    private void onHistory(View view) {
        listener.onHistory(event);
    }

    private void onAddLike(View view) {
        listener.onAddLike(event);
    }

    private void addDetailView(Event event) {
        Context context = itemView.getContext();
        EventType type = event.getType();

        ImageView eventIcon = itemView.findViewById(R.id.eventIcon);
        Drawable drawable = EventUI.getEventDrawable(type, context);
        eventIcon.setImageDrawable(drawable);

        String text = LocalizationService.getEventTypeName(context, type);
        if (EventTypeFactory.getAreas(type).size() > 1) {
            text = LocalizationService.getAreaName(event.getArea()) + " " + text;
        }

        TextView eventTypeText = itemView.findViewById(R.id.eventTypeText);
        eventTypeText.setText(text);

        TextView eventDetailText = itemView.findViewById(R.id.eventDetailText);
        eventDetailText.setText(EventUI.getEventDescription(event, context));
    }
}
