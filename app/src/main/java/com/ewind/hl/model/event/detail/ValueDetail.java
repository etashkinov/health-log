package com.ewind.hl.model.event.detail;

import com.ewind.hl.model.event.EventType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by etashkinov on 12.06.2018.
 */
public class ValueDetail implements EventDetail {

    private final EventType type;
    private final BigDecimal value;

    @JsonCreator
    public ValueDetail(
            @JsonProperty("type") EventType type,
            @JsonProperty("value") BigDecimal value) {
        this.type = type;
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    @JsonIgnore
    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
