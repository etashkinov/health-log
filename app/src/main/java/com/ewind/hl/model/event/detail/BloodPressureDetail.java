package com.ewind.hl.model.event.detail;

import com.ewind.hl.model.event.EventType;

public class BloodPressureDetail implements EventDetail {

    private final int highPressure;
    private final int lowPressure;

    public BloodPressureDetail(int highPressure, int lowPressure) {
        this.highPressure = highPressure;
        this.lowPressure = lowPressure;
    }

    public int getHighPressure() {
        return highPressure;
    }

    public int getLowPressure() {
        return lowPressure;
    }

    @Override
    public EventType getType() {
        return EventType.BLOOD_PRESSURE;
    }

    @Override
    public String toString() {
        return lowPressure + "x" + highPressure;
    }
}
