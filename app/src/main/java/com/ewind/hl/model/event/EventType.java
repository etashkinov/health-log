package com.ewind.hl.model.event;

import com.ewind.hl.model.event.detail.MoodDetail;

public enum EventType {
    MOOD(MoodDetail.),
    ENERGY,
    HEART_BEAT,
    BLOOD_PRESSURE,
    PAIN(true),
    SWEAT,
    TREMOR,
    TEMPERATURE,
    EYE_SIGHT,
    EYE_PRESSURE;

    private final boolean propagateDown;

    EventType() {
        propagateDown = false;
    }

    EventType(boolean propagateDown) {
        this.propagateDown = propagateDown;
    }

    public boolean isPropagateDown() {
        return propagateDown;
    }
}
