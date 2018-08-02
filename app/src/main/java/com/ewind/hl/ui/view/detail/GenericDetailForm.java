package com.ewind.hl.ui.view.detail;

import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.ui.view.EventDetailForm;

public interface GenericDetailForm<T extends EventDetail> extends EventDetailForm<T> {
    void setEventType(EventType<T> eventType);
}
