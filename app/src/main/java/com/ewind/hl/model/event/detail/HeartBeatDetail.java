package com.ewind.hl.model.event.detail;

import java.math.BigDecimal;

public class HeartBeatDetail extends ValueDetail {

    public enum HeartBeatState {
        RELAXED,
        NORMAL,
        EXERCISE
    }

    private final HeartBeatState state;

    public HeartBeatDetail(int rate, HeartBeatState state) {
        super(BigDecimal.valueOf(rate));
        this.state = state;
    }

    public HeartBeatState getState() {
        return state;
    }

    @Override
    public String toString() {
        return getValue() + " at " + state + " state";
    }
}
