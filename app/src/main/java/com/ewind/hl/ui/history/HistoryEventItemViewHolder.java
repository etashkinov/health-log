package com.ewind.hl.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.ui.EventActionListener;
import com.ewind.hl.ui.EventItemViewHolder;
import com.ewind.hl.ui.event.EventUI;
import com.ewind.hl.ui.event.EventUIFactory;

public class HistoryEventItemViewHolder extends EventItemViewHolder {

    public HistoryEventItemViewHolder(ViewGroup parent, EventActionListener listener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_history_item, parent, false), listener);
        itemView.setOnClickListener(this::onUpdate);
    }

    @Override
    public void setEvent(Event event) {
       super.setEvent(event);

        addDetailView(event);
    }

    private boolean onUpdate(View view) {
        listener.onUpdate(event);
        return true;
    }

    private void addDetailView(Event event) {
        Context context = itemView.getContext();

        ImageView eventIcon = itemView.findViewById(R.id.eventIcon);
        int band = getBand(event);
        int drawable = context.getResources().getIdentifier("ic_severity_" + band, "drawable", context.getPackageName());
        eventIcon.setImageDrawable(context.getDrawable(drawable));

        EventType type = event.getType();
        EventUI ui = EventUIFactory.getUI(type);
        ViewGroup eventDetailContainer = itemView.findViewById(R.id.eventDetailContainer);
        eventDetailContainer.removeAllViews();
        View view = ui.getHistoryEventDetailView(event, eventDetailContainer);
        eventDetailContainer.addView(view);
    }
}
