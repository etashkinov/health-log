package com.ewind.hl.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventType;

public class EventItemViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = EventItemViewHolder.class.getName();
    private final EventActionListener listener;
    private final View addButton;

    private Event event;
    private FrameLayout eventDetailContainer;
    private final TextView eventDateTextView;

    public EventItemViewHolder(ViewGroup parent, EventActionListener listener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false));
        this.listener = listener;

        itemView.setOnLongClickListener(this::onUpdate);
        addButton = itemView.findViewById(R.id.addButton);
        addButton.setOnClickListener(this::onAddLike);

        eventDateTextView = itemView.findViewById(R.id.eventDateTextView);

        eventDetailContainer = itemView.findViewById(R.id.eventDetailContainer);
    }

    void setEvent(Event event) {
        this.event = event;

        addDetailView(event);
        eventDateTextView.setText(event.getDate().toString());

        if (!this.event.isExpired()) {
            addButton.setVisibility(View.INVISIBLE);
        }
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

        LayoutInflater.from(itemView.getContext()).inflate(R.layout.event_item_detail, eventDetailContainer);
        ImageView eventIcon = eventDetailContainer.findViewById(R.id.eventIcon);
        Drawable drawable = EventUI.getEventDrawable(type, context);
        eventIcon.setImageDrawable(drawable);

        String text = EventUI.getEventDescription(event, context);
        if (EventUI.isDefaultIcon(type, context)) {
            text = LocalizationService.getEventTypeName(type) + ": " + text;
        } else if (AreaFactory.getAreas(type).size() > 1) {
            text = LocalizationService.getAreaName(event.getArea()) + ": " + text;
        }

        TextView eventText = eventDetailContainer.findViewById(R.id.eventText);
        eventText.setText(text);
    }
}
