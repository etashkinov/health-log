package com.ewind.hl.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.ewind.hl.model.event.type.MeasurementEventType;

import java.math.BigDecimal;

public class MeasurementDetailForm<D extends ValueDetail, T extends MeasurementEventType<D>> extends LinearLayout implements GenericDetailForm<D, T> {

    private T type;

    private NumberPicker wholePicker;
    private NumberPicker fractionPicker;
    private TextView measurementUnit;
    private View fractionSeparator;

    public MeasurementDetailForm(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        wholePicker = findViewById(R.id.wholePicker);
        fractionPicker = findViewById(R.id.fractionPicker);
        fractionSeparator = findViewById(R.id.fractionSeparator);
        measurementUnit = findViewById(R.id.measurementUnit);
    }

    public void setEventType(T type) {
        this.type = type;

        wholePicker.setMinValue(type.getMin().intValue() - 1);
        wholePicker.setMaxValue(type.getMax().intValue() + 1);

        fractionPicker.setMinValue(0);
        fractionPicker.setMaxValue(9);

        if (type.getStep().compareTo(BigDecimal.ONE) >= 0) {
            fractionSeparator.setVisibility(GONE);
            fractionPicker.setVisibility(GONE);
            fractionPicker.setValue(0);
        }
        measurementUnit.setText(type.getUnit());
    }

    @Override
    public void setDetail(D detail) {
        int intValue = detail.getValue().intValue();
        wholePicker.setValue(intValue);
        fractionPicker.setValue((int)((detail.getValue().doubleValue() - intValue) * 10));
    }

    @Override
    public D getDetail() {
        return type.createDetail(BigDecimal.valueOf(
                wholePicker.getValue() +
                        fractionPicker.getValue()/10.0)
        );
    }

}
