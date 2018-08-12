package com.ewind.hl.model.event.detail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class BloodPressureDetail extends ValueDetail {

    private final int low;
    private final int high;

    @JsonCreator
    public BloodPressureDetail(@JsonProperty("low")int low, @JsonProperty("high")int high) {
        super(BigDecimal.valueOf(Math.sqrt(low * high)));
        this.low = low;
        this.high = high;
    }

    @Override
    @JsonIgnore
    public BigDecimal getValue() {
        return super.getValue();
    }

    public int getLow() {
        return low;
    }

    public int getHigh() {
        return high;
    }
}
