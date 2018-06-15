package com.ewind.hl.model.event.detail;

import com.ewind.hl.model.event.EventType;

import java.math.BigDecimal;

/**
 * Created by etashkinov on 12.06.2018.
 */
public class ValueDetail implements EventDetail {

    private final EventType type;
    private final BigDecimal value;

    public ValueDetail(EventType type, BigDecimal value) {
        this.type = type;
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
