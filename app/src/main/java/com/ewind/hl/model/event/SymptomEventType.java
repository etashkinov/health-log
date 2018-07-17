package com.ewind.hl.model.event;

import android.content.Context;

import com.ewind.hl.model.event.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.ValueDetail;

import java.text.DecimalFormat;

public class SymptomEventType<T extends ValueDetail> extends EventType<T> {

    public SymptomEventType(String name, EventConfig config) {
        this(name, (Class<T>) ValueDetail.class, config);
    }

    protected SymptomEventType(String name, Class<T> detailClass, EventConfig config) {
        super(name, detailClass, config);
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

    @Override
    public String getDescription(Event<T> event, Context context) {
        return new DecimalFormat("##").format(event.getDetail().getScore());
    }
}
