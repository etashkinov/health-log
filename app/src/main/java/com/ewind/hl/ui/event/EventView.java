package com.ewind.hl.ui.event;

import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.detail.EventDetail;

public interface EventView<D extends EventDetail> {
    void setEvent(Event<D> event);
}
