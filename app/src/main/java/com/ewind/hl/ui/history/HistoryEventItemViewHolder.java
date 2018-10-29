package com.ewind.hl.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ewind.hl.R;
import com.ewind.hl.ui.EventActionListener;
import com.ewind.hl.ui.EventItemViewHolder;
import com.ewind.hl.ui.UiHelper;

public class HistoryEventItemViewHolder extends EventItemViewHolder {

    private ImageView eventIcon;
    private ViewGroup eventDetailContainer;

    public HistoryEventItemViewHolder(ViewGroup parent, EventActionListener listener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_history_item, parent, false), listener);

        itemView.setOnClickListener(this::onUpdate);
        eventIcon = itemView.findViewById(R.id.eventIcon);
        eventDetailContainer = itemView.findViewById(R.id.eventDetailContainer);
    }

    @Override
    public void setEvent(EventItem eventItem) {
       super.setEvent(eventItem);

        addDetailView(eventItem);
    }

    private boolean onUpdate(View view) {
        listener.onUpdate(eventItem.event);
        return true;
    }

    private void addDetailView(EventItem eventItem) {
        eventIcon.setImageDrawable(eventItem.icon);
        UiHelper.setTint(eventIcon, eventItem.tint);

        eventDetailContainer.removeAllViews();
        eventDetailContainer.addView(eventItem.detailsView);
    }
}
