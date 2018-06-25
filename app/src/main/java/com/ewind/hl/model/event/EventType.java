package com.ewind.hl.model.event;

import com.ewind.hl.model.event.detail.BloodPressureDetail;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.detail.EyeSightDetail;
import com.ewind.hl.model.event.detail.HeartBeatDetail;
import com.ewind.hl.model.event.detail.MoodDetail;
import com.ewind.hl.model.event.detail.PainDetail;
import com.ewind.hl.model.event.detail.ValueDetail;

public enum EventType {
    MOOD(MoodDetail.class),
    ENERGY(ValueDetail.class),
    HEART_BEAT(HeartBeatDetail.class),
    BLOOD_PRESSURE(BloodPressureDetail.class),
    PAIN(PainDetail.class, true),
    SWEAT (ValueDetail.class, true),
    TREMOR(ValueDetail.class, true),
    TEMPERATURE(ValueDetail.class),
    EYE_SIGHT (EyeSightDetail.class),
    EYE_PRESSURE(ValueDetail.class),
    CONGESTION(ValueDetail.class),
    WATERY(ValueDetail.class);

    private final Class<? extends EventDetail> detailClass;
    private final boolean propagateDown;

    EventType(Class<? extends EventDetail> detailClass) {
        this(detailClass, false);
    }

    EventType(Class<? extends EventDetail> detailClass, boolean propagateDown) {
        this.detailClass = detailClass;
        this.propagateDown = propagateDown;
    }

    public boolean isPropagateDown() {
        return propagateDown;
    }

    public Class<? extends EventDetail>  getDetailClass() {
        return detailClass;
    }
}
