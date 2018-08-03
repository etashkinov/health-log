package com.ewind.hl.ui.event.def;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ewind.hl.R;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.ui.event.EventFormView;
import com.ewind.hl.ui.event.EventUI;
import com.ewind.hl.ui.event.EventView;

public class DefaultEventUI<D extends EventDetail> implements EventUI<D> {

    @Override
    public EventFormView<D> createForm(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (EventFormView<D>) inflater.inflate(R.layout.view_event_form, parent, false);
    }

    @Override
    public EventView<D> createLastEventItem(ViewGroup parent) {
        return null;
    }

    @Override
    public EventView<D> createHistoryEventItem(ViewGroup parent) {
        return null;
    }
}
