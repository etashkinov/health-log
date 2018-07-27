package com.ewind.hl.model.event;

import android.content.Context;

import com.ewind.hl.model.event.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.ValueDetail;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class SymptomEventType<D extends ValueDetail> extends EventType<D> {

    public SymptomEventType(String name, EventConfig config) {
        this(name, (Class<D>) ValueDetail.class, config);
    }

    protected SymptomEventType(String name, Class<D> detailClass, EventConfig config) {
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
    public String getDescription(Event<D> event, Context context) {
        return new DecimalFormat("##").format(event.getDetail().getScore());
    }
    
    @Override
    public boolean isAbnormal(Event<D> event) {
        return event.getDetail().getValue().compareTo(BigDecimal.valueOf(getNormal())) != 0;
    }
}
