package com.ewind.hl.ui.event;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.ui.history.HistoryItemDetailView;
import com.ewind.hl.ui.view.EventDetailForm;

public interface EventUI<D extends EventDetail> {
    EventDetailForm<D> getEventDetailForm(ViewGroup parent);

    Drawable getEventTypeDrawable(Context context);

    int getEventDrawableId(Context context);

    int getDrawableByName(String name, Context context);

    String getEventDescription(Event<D> event, Context context);

    View getLastEventDetailView(Event<D> event, ViewGroup parent);

    View getHistoryEventDetailView(Event event, ViewGroup parent);

    void setEventTint(ImageView image, Event event);

    HistoryItemDetailView createHistoryItemDetail(Context context);
}
