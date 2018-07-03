package com.ewind.hl.model.event;

import com.ewind.hl.model.event.detail.BloodPressureDetail;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.detail.EyeSightDetail;
import com.ewind.hl.model.event.detail.HeartBeatDetail;
import com.ewind.hl.model.event.detail.MoodDetail;
import com.ewind.hl.model.event.detail.PainDetail;
import com.ewind.hl.model.event.detail.ValueDetail;

import org.joda.time.Period;

import static org.joda.time.Period.days;
import static org.joda.time.Period.hours;
import static org.joda.time.Period.years;

public enum EventType {
    MOOD(MoodDetail.class, hours(6)),
    ENERGY(ValueDetail.class, days(1)),
    HEART_BEAT(HeartBeatDetail.class, hours(6)),
    BLOOD_PRESSURE(BloodPressureDetail.class, hours(6)),
    PAIN(PainDetail.class, true, hours(6)),
    SWEAT (ValueDetail.class, true, hours(6)),
    TREMOR(ValueDetail.class, true, hours(6)),
    TEMPERATURE(ValueDetail.class, hours(6)),
    EYE_SIGHT (EyeSightDetail.class, years(2)),
    EYE_PRESSURE(ValueDetail.class,  days(1)),
    CONGESTION(ValueDetail.class, hours(6)),
    WATERY(ValueDetail.class, hours(6));

    private final Class<? extends EventDetail> detailClass;
    private final boolean propagateDown;
    private final Period expiration;

    EventType(Class<? extends EventDetail> detailClass, Period expiration) {
        this(detailClass, false, expiration);
    }

    EventType(Class<? extends EventDetail> detailClass, boolean propagateDown, Period expiration) {
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

    public Period getExpiration() {
        return expiration;
    }
}
