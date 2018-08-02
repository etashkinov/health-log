package com.ewind.hl.model.event.type;

import com.ewind.hl.model.event.type.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.PainDetail;

import java.math.BigDecimal;
import java.util.Collections;

public class PainEventType extends SeverityEventType<PainDetail> {

    public PainEventType(String name, EventConfig config) {
        super(name, PainDetail.class, config);
    }

    @Override
    public PainDetail createDetail(int score) {
        return new PainDetail(BigDecimal.valueOf(score), Collections.emptyList());
    }
}
