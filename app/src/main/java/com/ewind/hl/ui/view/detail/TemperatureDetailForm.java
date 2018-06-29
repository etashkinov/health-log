package com.ewind.hl.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.ewind.hl.R;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.ewind.hl.ui.view.EventDetailForm;

import java.math.BigDecimal;

public class TemperatureDetailForm extends LinearLayout implements EventDetailForm<ValueDetail> {

    private NumberPicker temperatureWholePicker;
    private NumberPicker temperatureFractionPicker;

    public TemperatureDetailForm(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        temperatureWholePicker = findViewById(R.id.temperatureWholePicker);
        temperatureWholePicker.setMinValue(32);
        temperatureWholePicker.setMaxValue(42);

        temperatureFractionPicker = findViewById(R.id.temperatureFractionPicker);
        temperatureFractionPicker.setMinValue(0);
        temperatureFractionPicker.setMaxValue(9);
    }

    @Override
    public void setDetail(ValueDetail detail) {
        int intValue = detail.getValue().intValue();
        temperatureWholePicker.setValue(intValue);
        temperatureFractionPicker.setValue((int)((detail.getValue().doubleValue() - intValue) * 10));
    }

    @Override
    public ValueDetail getDetail() {
        return new ValueDetail(BigDecimal.valueOf(
                temperatureWholePicker.getValue() +
                        temperatureFractionPicker.getValue()/10.0)
        );
    }

}
