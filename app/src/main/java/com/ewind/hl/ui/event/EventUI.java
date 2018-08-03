package com.ewind.hl.ui.event;

import android.view.ViewGroup;

import com.ewind.hl.model.event.detail.EventDetail;

public interface EventUI<D extends EventDetail> {
    EventFormView<D> createForm(ViewGroup parent);
    EventView<D> createLastEventItem(ViewGroup parent);
    EventView<D>  createHistoryEventItem(ViewGroup parent);
}
