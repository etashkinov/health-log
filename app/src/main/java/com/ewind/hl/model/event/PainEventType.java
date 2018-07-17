package com.ewind.hl.model.event;

import com.ewind.hl.model.event.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.PainDetail;

public class PainEventType extends SeverityEventType<PainDetail> {

    protected PainEventType(String name, EventConfig config) {
        super(name, PainDetail.class, config);
    }
}
