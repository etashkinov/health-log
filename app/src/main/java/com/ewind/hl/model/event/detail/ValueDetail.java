package com.ewind.hl.model.event.detail;

import com.ewind.hl.model.event.EventType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by etashkinov on 12.06.2018.
 */
public class ValueDetail implements EventDetail {

    private final EventType valueType;
    private final BigDecimal value;

    @JsonCreator
    public ValueDetail(
            @JsonProperty("valueType") EventType valueType,
            @JsonProperty("value") BigDecimal value) {
        this.valueType = valueType;
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public double getScore() {
        return getValue().doubleValue();
    }

    @Override
    public EventType getType() {
        return getValueType();
    }

    public EventType getValueType() {
        return valueType;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
