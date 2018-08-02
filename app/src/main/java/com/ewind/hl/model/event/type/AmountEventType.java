package com.ewind.hl.model.event.type;

import com.ewind.hl.model.event.detail.ValueDetail;

public class AmountEventType<T extends ValueDetail> extends EnumEventType<AmountEventType.Amount, T> {

    public enum Amount {
        NONE,
        BARELY,
        LITTLE,
        MODERATE,
        A_LOT,
        HOARD
    }

    public AmountEventType(String name, EventTypeFactory.EventConfig config) {
        super(name, config, Amount.class);
    }

    protected AmountEventType(String name, Class<T> detailClass, EventTypeFactory.EventConfig config) {
        super(name, config, detailClass, Amount.class);
    }
}
