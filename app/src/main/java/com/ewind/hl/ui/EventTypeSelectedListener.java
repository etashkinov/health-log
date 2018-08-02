package com.ewind.hl.ui;

import com.ewind.hl.model.event.type.EventType;

public interface EventTypeSelectedListener {
    void onEventTypeSelected(EventType<?> type);
}
