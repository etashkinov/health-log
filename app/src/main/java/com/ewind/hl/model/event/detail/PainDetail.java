package com.ewind.hl.model.event.detail;

import com.ewind.hl.model.event.EventType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class PainDetail extends ValueDetail {

    private final List<PainType> painTypes;

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

    @JsonCreator
    public PainDetail(
            @JsonProperty("value") BigDecimal value,
            @JsonProperty("painTypes") List<PainType> painTypes) {
        super(EventType.PAIN, value);
        this.painTypes = Collections.unmodifiableList(painTypes);
    }

    public List<PainType> getPainTypes() {
        return painTypes;
    }

    @Override
    public String toString() {
        return painTypes + ", severity " + getValue() + "%";
    }
}
