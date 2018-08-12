package com.ewind.hl.model.event.type;

import android.content.Context;

import com.ewind.hl.model.event.detail.BloodPressureDetail;
import com.ewind.hl.model.event.type.EventTypeFactory.EventConfig;

public class BloodPressureEventType extends MeasurementEventType<BloodPressureDetail> {

    public BloodPressureEventType(String name, EventConfig config) {
        super(name, BloodPressureDetail.class, config);
    }

    @Override
    protected BloodPressureDetail createNormalDetail() {
        return new BloodPressureDetail(70, 100);
    }

    @Override
    public String getDescription(BloodPressureDetail detail, Context context) {
        return detail.getHigh() + "/" + detail.getLow();
    }
}
