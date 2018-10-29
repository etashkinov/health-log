package com.ewind.hl.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ewind.hl.R;

public class LastEventItemViewHolder extends EventItemViewHolder {
    private final View addButton;
    private ImageView eventIcon;
    private ViewGroup eventDetailContainer;

    public LastEventItemViewHolder(ViewGroup parent, EventActionListener listener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false), listener);

        addButton = itemView.findViewById(R.id.addButton);
        eventIcon = itemView.findViewById(R.id.eventIcon);
        eventDetailContainer = itemView.findViewById(R.id.eventDetailContainer);
    }

    @Override
    public void setEvent(EventItem eventItem) {
        super.setEvent(eventItem);

        addDetailView(eventItem);

        boolean updateNeeded = eventItem.event.updateNeeded();
        addButton.setVisibility(updateNeeded ? View.VISIBLE : View.INVISIBLE);
        itemView.setOnClickListener(updateNeeded ? this::onAddLike : this::onHistory);
    }

    private void onHistory(View view) {
        listener.onHistory(eventItem.event);
    }

    private void onAddLike(View view) {
        listener.onAddLike(eventItem.event);
    }

    private void addDetailView(EventItem eventItem) {
        eventIcon.setImageDrawable(eventItem.icon);
        UiHelper.setTint(eventIcon, eventItem.tint);

        eventDetailContainer.removeAllViews();
        eventDetailContainer.addView(eventItem.detailsView);
    }
}
