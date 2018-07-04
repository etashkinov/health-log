package com.ewind.hl.model.event;

import com.ewind.hl.model.event.detail.BloodPressureDetail;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.detail.EyeSightDetail;
import com.ewind.hl.model.event.detail.HeartBeatDetail;
import com.ewind.hl.model.event.detail.MoodDetail;
import com.ewind.hl.model.event.detail.PainDetail;
import com.ewind.hl.model.event.detail.ValueDetail;

import org.joda.time.Period;

import static com.ewind.hl.model.event.EventDate.DAY;
import static com.ewind.hl.model.event.EventDate.QUARTER;
import static org.joda.time.Period.years;

public enum EventType {
    
    MOOD(MoodDetail.class, QUARTER),
    ENERGY(ValueDetail.class, DAY),
    HEART_BEAT(HeartBeatDetail.class, QUARTER),
    BLOOD_PRESSURE(BloodPressureDetail.class, QUARTER),
    PAIN(PainDetail.class, true, QUARTER),
    SWEAT (ValueDetail.class, true, QUARTER),
    TREMOR(ValueDetail.class, true, QUARTER),
    TEMPERATURE(ValueDetail.class, QUARTER),
    EYE_SIGHT (EyeSightDetail.class, years(2)),
    EYE_PRESSURE(ValueDetail.class,  DAY),
    CONGESTION(ValueDetail.class, QUARTER),
    WATERY(ValueDetail.class, QUARTER);

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
