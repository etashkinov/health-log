package com.ewind.hl.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.detail.BloodPressureDetail;
import com.ewind.hl.model.event.type.BloodPressureEventType;
import com.ewind.hl.model.event.type.EventTypeFactory;

public class BloodPressureDetailForm extends LinearLayout implements GenericDetailForm<BloodPressureDetail, BloodPressureEventType> {
    private BloodPressureEventType type;

    private NumberPicker lowPicker;
    private NumberPicker highPicker;
    private TextView measurementUnit;

    public BloodPressureDetailForm(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        lowPicker = findViewById(R.id.lowPicker);
        highPicker = findViewById(R.id.highPicker);
        measurementUnit = findViewById(R.id.measurementUnit);

        setEventType(EventTypeFactory.get("blood_pressure"));
    }

    @Override
    public void setDetail(BloodPressureDetail detail) {
        lowPicker.setValue(detail.getLow());
        highPicker.setValue(detail.getHigh());
    }

    @Override
    public BloodPressureDetail getDetail() {
        return new BloodPressureDetail(lowPicker.getValue(),highPicker.getValue());
    }

    @Override
    public void setEventType(BloodPressureEventType eventType) {
        this.type = eventType;

        lowPicker.setMinValue(30);
        lowPicker.setMaxValue(110);

        highPicker.setMinValue(60);
        highPicker.setMaxValue(200);

        measurementUnit.setText(type.getUnit());
    }
}
