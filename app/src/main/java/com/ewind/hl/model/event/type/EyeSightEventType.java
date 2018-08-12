package com.ewind.hl.model.event.type;

import android.content.Context;

import com.ewind.hl.model.event.Score;
import com.ewind.hl.model.event.detail.EyeSightDetail;
import com.ewind.hl.model.event.detail.EyeSightDetail.EyeSight;

import java.math.BigDecimal;

public class EyeSightEventType extends EventType<EyeSightDetail> {

    private static final int MAX_POWER = 15;

    public EyeSightEventType(String name, EventTypeFactory.EventConfig config) {
        super(name, EyeSightDetail.class, config);
    }

    @Override
    protected EyeSightDetail createNormalDetail() {
        return new EyeSightDetail(
                new EyeSight(BigDecimal.ZERO, null, null),
                new EyeSight(BigDecimal.ZERO, null, null)
        );
    }

    @Override
    public Score getScore(EyeSightDetail detail) {
        return new Score(Math.max(getScore(detail.getLeft()), getScore(detail.getRight())));
    }

    private int getScore(EyeSight eyeSight) {
        BigDecimal power = eyeSight.getSphere().abs();

        if (eyeSight.getCylinder() != null) {
            power = power.add(eyeSight.getCylinder().abs());
        }

        return power.intValue() * 100 / MAX_POWER;
    }

    @Override
    public String getDescription(EyeSightDetail detail, Context context) {
        return detail.getLeft().getSphere().max(detail.getRight().getSphere()).toString();
    }
}
