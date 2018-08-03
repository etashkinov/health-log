package com.ewind.hl.ui.event;

import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.detail.EventDetail;

public interface EventFormView<D extends EventDetail> extends EventView<D> {
    Event<D> getEvent();
}
