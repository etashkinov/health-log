package com.ewind.hl.ui.view.detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ewind.hl.R;
import com.ewind.hl.model.event.detail.EyeSightDetail;
import com.ewind.hl.model.event.detail.EyeSightDetail.EyeSight;
import com.ewind.hl.ui.view.EventDetailForm;

import java.math.BigDecimal;

public class EyeSightDetailForm extends LinearLayout implements EventDetailForm<EyeSightDetail> {

    private EditText leftSphere;
    private EditText leftCylinder;
    private EditText leftAxis;

    private EditText rightSphere;
    private EditText rightCylinder;
    private EditText rightAxis;

    public EyeSightDetailForm(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        leftSphere = findViewById(R.id.eyeSightLeftSphere);
        leftCylinder = findViewById(R.id.eyeSightLeftCylinder);
        leftAxis = findViewById(R.id.eyeSightLeftAxis);

        rightSphere = findViewById(R.id.eyeSightRightSphere);
        rightCylinder = findViewById(R.id.eyeSightRightCylinder);
        rightAxis = findViewById(R.id.eyeSightRightAxis);
    }

    @Override
    public void setDetail(EyeSightDetail detail) {
        EyeSight leftEyeSight = detail.getLeft();
        leftSphere.setText(toString(leftEyeSight.getSphere()));
        leftCylinder.setText(toString(leftEyeSight.getCylinder()));
        leftAxis.setText(leftEyeSight.getAxis() == null ? "" : String.valueOf(leftEyeSight.getAxis()));

        EyeSight rightEyeSight = detail.getRight();
        rightSphere.setText(toString(rightEyeSight.getSphere()));
        rightCylinder.setText(toString(rightEyeSight.getCylinder()));
        rightAxis.setText(rightEyeSight.getAxis() == null ? "" : String.valueOf(rightEyeSight.getAxis()));
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
        EyeSight leftEyeSight = getEyeSight(leftSphere, leftCylinder, leftAxis);
        EyeSight rightEyeSight = getEyeSight(rightSphere, rightCylinder, rightAxis);
        return new EyeSightDetail(leftEyeSight, rightEyeSight);
    }

    @NonNull
    private EyeSight getEyeSight(EditText sphere, EditText cylinder, EditText axis) {
        BigDecimal axisValue = toBigDecimal(axis);
        return new EyeSight(
                toBigDecimal(sphere),
                toBigDecimal(cylinder),
                axisValue == null ? null : axisValue.intValue());
    }
}
