package com.ewind.hl.model.event;

import android.content.Context;

import com.ewind.hl.model.event.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.ValueDetail;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class SymptomEventType<D extends ValueDetail> extends EventType<D> {

    public SymptomEventType(String name, EventConfig config) {
        this(name, (Class<D>) ValueDetail.class, config);
    }

    protected SymptomEventType(String name, Class<D> detailClass, EventConfig config) {
        super(name, detailClass, config);
    }

    @Override
    protected D createNormalDetail() {
        return createDetail(getNormal());
    }

    public D createDetail(int value) {
        try {
            Constructor<D> constructor = getDetailClass().getConstructor(BigDecimal.class);
            return constructor.newInstance(BigDecimal.valueOf(value));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create value details for " + this, e);
        }
    }

    public int getMaximum() {
        return 100;
    }

    public int getNormal() {
        return 0;
    }

    @Override
    public String getDescription(D detail, Context context) {
        return new DecimalFormat("##").format(getScore(detail).getValue());
    }

    @Override
    public Score getScore(D detail) {
        return new Score(detail.getValue().intValue());
    }
}
