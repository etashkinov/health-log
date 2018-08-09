package com.ewind.hl.ui.event;

import com.ewind.hl.R;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.MeasurementEventType;

public class MeasurementEventUI<D extends ValueDetail, T extends MeasurementEventType<D>> extends DefaultEventUI<D, T> {

    public MeasurementEventUI(EventType<D> type) {
        super(type);
    }

    @Override
    protected int getDefaultEventDetailLayoutId() {
        return R.layout.event_measurement_form;
    }
}
