package com.ewind.hl.model.event.detail;

import com.ewind.hl.model.event.EventType;

import java.math.BigDecimal;

public class PainDetail extends ValueDetail {

    private final PainType painType;

    public enum PainType {
        ACHING,
        CRAMPING,
        FEARFUL,
        GNAWING,
        HEAVY,
        BURNING,
        SHARP,
        SHOOTING,
        SICKENING,
        SPLITTING,
        STABBING,
        CRUEL,
        TENDER,
        THROBBING,
        EXHAUSTING,
    }

    public PainDetail(BigDecimal value, PainType painType) {
        super(EventType.PAIN, value);
        this.painType = painType;
    }

    public PainType getPainType() {
        return painType;
    }

    @Override
    public String toString() {
        return painType + ", severity " + getValue() + "%";
    }
}
