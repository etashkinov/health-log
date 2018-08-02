package com.ewind.hl.model.event.type;

import com.ewind.hl.model.event.type.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.ValueDetail;

public class SizeEventType<T extends ValueDetail> extends EnumEventType<SizeEventType.Size, T> {

    public enum Size {
        NONE,
        TINY,
        SMALL,
        MEDIUM,
        LARGE,
        ENORMOUS
    }

    protected SizeEventType(String name, Class<T> detailClass, EventConfig config) {
        super(name, config, detailClass, Size.class);
    }

    public SizeEventType(String name, EventConfig config) {
        super(name, config, Size.class);
    }
}
