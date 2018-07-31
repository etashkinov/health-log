package com.ewind.hl.ui.view.detail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.SymptomEventType;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.ewind.hl.ui.view.SeverityAdapter;

public class EnumDetailForm<T extends ValueDetail> extends LinearLayout implements GenericDetailForm<T> {

    private static final int SIZE = 6;

    private TextView textView;
    private SymptomEventType<T> eventType;
    private SeverityAdapter adapter;
    private int step;

    public EnumDetailForm(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setEventType(EventType<T> eventType) {
        this.eventType = (SymptomEventType<T>) eventType;
        this.step = this.eventType.getMaximum() / (SIZE - 1);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        RecyclerView eventEnumContainer = findViewById(R.id.eventEnumContainer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(HORIZONTAL);
        eventEnumContainer.setLayoutManager(layoutManager);
        adapter = new SeverityAdapter(SIZE, v -> updateText());
        eventEnumContainer.setAdapter(adapter);

        textView = findViewById(R.id.valueText);
        updateText();
    }

    private void updateText() {
        textView.setText(eventType.getDescription(getDetail(), getContext()));
    }

    @Override
    public void setDetail(T detail) {
        adapter.setValue(detail.getValue().intValue() / step, getContext());
    }

    @Override
    public T getDetail() {
        return eventType.createDetail(getValue());
    }

    public int getValue() {
        return adapter.getValue() * step;
    }
}
