package com.ewind.hl.model.event.type;

import com.ewind.hl.model.event.type.EventTypeFactory.EventConfig;
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

    public SeverityEventType(String name, EventConfig config) {
        super(name, config, Severity.class);
    }
}
