package com.ewind.hl.ui.view.detail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ewind.hl.R;
import com.ewind.hl.model.event.detail.EyeSightDetail;
import com.ewind.hl.ui.view.EventDetailForm;

import java.math.BigDecimal;

public class EyeSightDetailForm extends LinearLayout implements EventDetailForm<EyeSightDetail> {

    private EditText sphere;
    private EditText cylinder;
    private EditText axis;

    public EyeSightDetailForm(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        sphere = findViewById(R.id.eyeSightSphere);
        cylinder = findViewById(R.id.eyeSightCylinder);
        axis = findViewById(R.id.eyeSightAxis);
    }

    @Override
    public void setDetail(EyeSightDetail detail) {
        BigDecimal value = detail.getSphere();
        sphere.setText(toString(value));
        cylinder.setText(toString(detail.getCylinder()));
        axis.setText(detail.getAxis() == null ? "" : String.valueOf(detail.getAxis()));
    }

    private String toString(BigDecimal value) {
        return value == null ? "" : value.toString();
    }

    @Nullable
    private BigDecimal toBigDecimal(EditText editText) {
        String value = editText.getText().toString();
        return value.trim().isEmpty() ? null : new BigDecimal(value);
    }

    @Override
    public EyeSightDetail getDetail() {
        BigDecimal axisValue = toBigDecimal(axis);
        return new EyeSightDetail(
                toBigDecimal(sphere),
                toBigDecimal(cylinder),
                axisValue == null ? null : axisValue.intValue());
    }
}
