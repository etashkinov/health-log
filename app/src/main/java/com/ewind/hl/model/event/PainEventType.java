package com.ewind.hl.model.event;

import com.ewind.hl.model.event.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.PainDetail;

public class PainEventType extends EnumEventType<SeverityEventType.Severity, PainDetail> {

    public PainEventType(String name, EventConfig config) {
        super(name, PainDetail.class, config);
    }
}
