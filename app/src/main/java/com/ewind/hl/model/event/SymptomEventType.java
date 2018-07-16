package com.ewind.hl.model.event;

import com.ewind.hl.model.event.detail.ValueDetail;

import org.joda.time.Period;

import java.util.Set;

public class SymptomEventType<T extends ValueDetail> extends EventType<T> {

    public SymptomEventType(String name, Period expiration, Accuracy accuracy, Set<String> areas, Class<T> detailClass, boolean propagateDown) {
        super(name, expiration, accuracy, areas, detailClass, propagateDown);
    }

    public int getMaximum() {
        return 100;
    }

    public int getMinimum() {
        return 0;
    }

    public int getNormal() {
        return 0;
    }
}
