package com.ewind.hl.model.event;

import com.ewind.hl.model.event.detail.BloodPressureDetail;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.detail.EyeSightDetail;
import com.ewind.hl.model.event.detail.HeartBeatDetail;
import com.ewind.hl.model.event.detail.MoodDetail;
import com.ewind.hl.model.event.detail.PainDetail;
import com.ewind.hl.model.event.detail.ValueDetail;

import static com.ewind.hl.model.event.ChronoUnit.*;
import static com.ewind.hl.model.event.Duration.of;

public enum EventType {
    MOOD(MoodDetail.class, of(1, QUARTER)),
    ENERGY(ValueDetail.class, of(1, DAYS)),
    HEART_BEAT(HeartBeatDetail.class, of(1, QUARTER)),
    BLOOD_PRESSURE(BloodPressureDetail.class, of(1, QUARTER)),
    PAIN(PainDetail.class, true, of(1, QUARTER)),
    SWEAT (ValueDetail.class, true, of(1, QUARTER)),
    TREMOR(ValueDetail.class, true, of(1, QUARTER)),
    TEMPERATURE(ValueDetail.class, of(4, HOURS)),
    EYE_SIGHT (EyeSightDetail.class, of(2, YEARS)),
    EYE_PRESSURE(ValueDetail.class,  of(1, DAYS)),
    CONGESTION(ValueDetail.class, of(1, QUARTER)),
    WATERY(ValueDetail.class, of(1, QUARTER));

    private final Class<? extends EventDetail> detailClass;
    private final boolean propagateDown;
    private final Duration expiration;

    EventType(Class<? extends EventDetail> detailClass, Duration expiration) {
        this(detailClass, false, expiration);
    }

    EventType(Class<? extends EventDetail> detailClass, boolean propagateDown, Duration expiration) {
        this.detailClass = detailClass;
        this.propagateDown = propagateDown;
        this.expiration = expiration;
    }

    public boolean isPropagateDown() {
        return propagateDown;
    }

    public Class<? extends EventDetail>  getDetailClass() {
        return detailClass;
    }

    public Duration getExpiration() {
        return expiration;
    }
}
