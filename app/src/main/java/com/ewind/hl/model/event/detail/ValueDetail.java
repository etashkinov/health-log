package com.ewind.hl.model.event.detail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by etashkinov on 12.06.2018.
 */
public class ValueDetail implements EventDetail {

    private final BigDecimal value;

    @JsonCreator
    public ValueDetail(@JsonProperty("value") BigDecimal value) {
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
    public Object getDescription() {
        return getScore();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
