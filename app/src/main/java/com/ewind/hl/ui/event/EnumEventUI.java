package com.ewind.hl.ui.event;

import com.ewind.hl.R;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.ewind.hl.model.event.type.EventType;

public class EnumEventUI<D extends ValueDetail> extends DefaultEventUI<D> {

    public EnumEventUI(EventType<D> type) {
        super(type);
    }

    @Override
    protected int getDefaultEventDetailLayoutId() {
        return R.layout.event_enum_form;
    }
}
