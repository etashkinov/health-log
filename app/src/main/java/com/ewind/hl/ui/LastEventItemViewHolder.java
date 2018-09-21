package com.ewind.hl.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.ui.event.EventUI;
import com.ewind.hl.ui.event.EventUIFactory;

public class LastEventItemViewHolder extends EventItemViewHolder {
    private final View addButton;

    public LastEventItemViewHolder(ViewGroup parent, EventActionListener listener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false), listener);

        addButton = itemView.findViewById(R.id.addButton);
    }

    @Override
    public void setEvent(Event event) {
        super.setEvent(event);

        addDetailView(event);

        boolean updateNeeded = event.updateNeeded();
        addButton.setVisibility(updateNeeded ? View.VISIBLE : View.INVISIBLE);
        itemView.setOnClickListener(updateNeeded ? this::onAddLike : this::onHistory);
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
        EventUI ui = EventUIFactory.getUI(type);
        ImageView eventIcon = itemView.findViewById(R.id.eventIcon);
        Drawable drawable = ui.getEventTypeDrawable(context);
        eventIcon.setImageDrawable(drawable);
        int eventTint = getEventTint(event, context);
        if (eventTint != 0) {
            int color = ContextCompat.getColor(itemView.getContext(), eventTint);
            Log.i(TAG, "Tint for " + type.getName() + ": " + color);
            eventIcon.setImageTintList(ColorStateList.valueOf(color));
            eventIcon.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
        }

        ViewGroup eventDetailContainer = itemView.findViewById(R.id.eventDetailContainer);
        eventDetailContainer.removeAllViews();
        View view = ui.getLastEventDetailView(event, eventDetailContainer);
        eventDetailContainer.addView(view);
    }

    private int getEventTint(Event event, Context context) {
        int band = getBand(event);
        int result = context.getResources().getIdentifier("severity" + band, "color", context.getPackageName());
        return result == 0 ? R.color.colorAccent : result;
    }
}
