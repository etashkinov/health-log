package com.ewind.hl.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EnumEventType;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.ewind.hl.ui.LocalizationService;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;

public class ValueDetailForm<T extends ValueDetail> extends LinearLayout implements GenericDetailForm<T>, SeekBar.OnSeekBarChangeListener {

    private SeekBar seekBar;
    private TextView textView;
    private EventType<T> eventType;

    public ValueDetailForm(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setEventType(EventType<T> eventType) {
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
    public void setDetail(T detail) {
        seekBar.setProgress(detail.getValue().intValue());
    }

    @Override
    public T getDetail() {
        try {
            Constructor<T> constructor = eventType.getDetailClass().getConstructor(BigDecimal.class);
            return constructor.newInstance(getValue());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create value details for " + eventType, e);
        }
    }

    protected BigDecimal getValue() {
        return BigDecimal.valueOf(seekBar.getProgress());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String text;
        if (eventType instanceof EnumEventType) {
            Enum type = ((EnumEventType) eventType).getType(BigDecimal.valueOf(progress));
            text = LocalizationService.snakeCaseToReadable(type.name());
        } else {
            text = String.valueOf(progress);
        }
        textView.setText(text);
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
