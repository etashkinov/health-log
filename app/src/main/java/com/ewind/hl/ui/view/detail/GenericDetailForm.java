package com.ewind.hl.ui.view.detail;

import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.ui.view.EventDetailForm;

public interface GenericDetailForm<D extends EventDetail, T extends EventType<D>> extends EventDetailForm<D> {
    void setEventType(T eventType);
}
