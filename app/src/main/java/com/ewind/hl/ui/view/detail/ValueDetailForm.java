package com.ewind.hl.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.ewind.hl.ui.view.EventDetailForm;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;

public class ValueDetailForm extends LinearLayout implements EventDetailForm<ValueDetail>, SeekBar.OnSeekBarChangeListener {

    private SeekBar seekBar;
    private TextView textView;
    private EventType eventType;

    public ValueDetailForm(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        seekBar = findViewById(R.id.valueSeekBar);

        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(0);

        textView = findViewById(R.id.valueText);
        textView.setText("");
    }

    @Override
    public void setDetail(ValueDetail detail) {
        seekBar.setProgress(detail.getValue().intValue());
    }

    @Override
    public ValueDetail getDetail() {
        try {
            Constructor<? extends EventDetail> constructor = eventType.getDetailClass().getConstructor(EventType.class, BigDecimal.class);
            return (ValueDetail) constructor.newInstance(eventType, BigDecimal.valueOf(seekBar.getProgress()));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create value details for " + eventType, e);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textView.setText(String.valueOf(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // do nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // do nothing
    }
}
