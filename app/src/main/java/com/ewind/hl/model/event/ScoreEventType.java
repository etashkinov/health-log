package com.ewind.hl.model.event;

import android.content.Context;

import com.ewind.hl.model.event.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.ValueDetail;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ScoreEventType<D extends ValueDetail> extends EventType<D> {

    public ScoreEventType(String name, EventConfig config) {
        this(name, (Class<D>) ValueDetail.class, config);
    }

    protected ScoreEventType(String name, Class<D> detailClass, EventConfig config) {
        super(name, detailClass, config);
    }

    @Override
    protected D createNormalDetail() {
        return createDetail(Score.MIN);
    }

    public D createDetail(int score) {
        try {
            Constructor<D> constructor = getDetailClass().getConstructor(BigDecimal.class);
            return constructor.newInstance(BigDecimal.valueOf(score));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create value details for " + this, e);
        }
    }

    @Override
    public String getDescription(D detail, Context context) {
        return detail == null ? null : getDescription(detail.getValue(), context);
    }

    public String getDescription(BigDecimal value, Context context) {
        return getDescription(value.intValue(), context);
    }

    public String getDescription(int score, Context context) {
        return new DecimalFormat("##").format(score);
    }

    @Override
    public Score getScore(D detail) {
        return new Score(detail.getValue().intValue());
    }
}
