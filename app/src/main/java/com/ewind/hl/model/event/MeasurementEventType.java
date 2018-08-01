package com.ewind.hl.model.event;

import android.content.Context;

import com.ewind.hl.model.event.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.ValueDetail;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MeasurementEventType<D extends ValueDetail> extends EventType<D> {

    private BigDecimal min;
    private BigDecimal max;

    private BigDecimal normMax;
    private BigDecimal normal;
    private BigDecimal normMin;

    private BigDecimal step;

    public MeasurementEventType(String name, EventConfig config) {
        this(name, (Class<D>) ValueDetail.class, config);
    }

    protected MeasurementEventType(String name, Class<D> detailClass, EventConfig config) {
        super(name, detailClass, config);
        min = config.getValue().getMin();
        max = config.getValue().getMax();
        normMax = config.getValue().getNormalMax();
        normal = config.getValue().getNormal();
        normMin = config.getValue().getNormalMin();
        step = config.getValue().getStep();
    }

    @Override
    protected D createNormalDetail() {
        return createDetail(normal);
    }

    public D createDetail(BigDecimal value) {
        try {
            Constructor<D> constructor = getDetailClass().getConstructor(BigDecimal.class);
            return constructor.newInstance(value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create value details for " + this, e);
        }
    }

    @Override
    public Score getScore(D detail) {
        BigDecimal value = detail.getValue();
        BigDecimal toMin = value.subtract(normMin);
        if (toMin.signum() < 0) {
            return new Score(toMin.negate().divide(normMin.subtract(min)));
        }

        BigDecimal toMax = normMin.subtract(value);
        if (toMax.signum() < 0) {
            BigDecimal negate = toMax.negate();
            BigDecimal subtract = max.subtract(normMax);
            BigDecimal divide = negate.divide(subtract, RoundingMode.HALF_UP);
            return new Score(divide);
        }

        return new Score(0);
    }

    @Override
    public String getDescription(D detail, Context context) {
        return new DecimalFormat("##.#").format(detail.getValue());
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public BigDecimal getNormMax() {
        return normMax;
    }

    public BigDecimal getNormMin() {
        return normMin;
    }

    public BigDecimal getStep() {
        return step;
    }
}