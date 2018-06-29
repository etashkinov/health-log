package com.ewind.hl.ui;

import com.ewind.hl.model.event.Event;

public interface EventChangedListener {
    void onEventCreated(Event event);
    void onEventUpdated(Event event);
    void onEventDeleted(Event event);
}
