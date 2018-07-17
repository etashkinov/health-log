package com.ewind.hl.model.event;

import com.ewind.hl.model.event.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.ValueDetail;

public class SeverityEventType<T extends ValueDetail> extends EnumEventType<SeverityEventType.Severity, T> {

    public enum Severity {
        NONE,
        A_BIT,
        MILD,
        MODERATE,
        SEVERE,
        INTOLERABLE
    }

    protected SeverityEventType(String name, Class<T> detailClass, EventConfig config) {
        super(name, config, detailClass, Severity.class);
    }

    protected SeverityEventType(String name, EventConfig config) {
        super(name, config, Severity.class);
    }
}
