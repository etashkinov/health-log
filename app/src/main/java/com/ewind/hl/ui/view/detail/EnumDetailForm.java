package com.ewind.hl.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.Score;
import com.ewind.hl.model.event.SymptomEventType;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.ewind.hl.ui.EventUI;

import java.util.ArrayList;
import java.util.List;

public class EnumDetailForm<T extends ValueDetail> extends LinearLayout implements GenericDetailForm<T> {

    private List<ImageView> severityViews;
    private int value;
    private TextView textView;
    private SymptomEventType<T> eventType;

    public EnumDetailForm(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setEventType(EventType<T> eventType) {
        this.eventType = (SymptomEventType<T>) eventType;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        severityViews = new ArrayList<>(6);
        initSeverityView(R.id.eventSeverity0);
        initSeverityView(R.id.eventSeverity1);
        initSeverityView(R.id.eventSeverity2);
        initSeverityView(R.id.eventSeverity3);
        initSeverityView(R.id.eventSeverity4);
        initSeverityView(R.id.eventSeverity5);

        textView = findViewById(R.id.valueText);
        textView.setText("");
    }

    private void initSeverityView(int severityViewId) {
        ImageView view = findViewById(severityViewId);
        view.setOnClickListener(this::onSeverityViewClick);
        view.setImageDrawable(getContext().getDrawable(R.drawable.ic_severity_none));
        severityViews.add(view);
    }

    private void onSeverityViewClick(View view) {
        setValue(severityViews.indexOf(view));
    }

    private void setValue(int value) {
        for (int i = 0; i < severityViews.size(); i++) {
            int drawableId = getDrawableId(value, i);
            severityViews.get(i).setImageDrawable(getContext().getDrawable(drawableId));
        }

        this.value = value;
        textView.setText(eventType.getDescription(getDetail(), getContext()));
    }

    private int getDrawableId(int value, int imageViewIndex) {
        int valueDrawable = EventUI.getDrawableByName("ic_severity_" + value, getContext());
        return imageViewIndex <= value ? valueDrawable : R.drawable.ic_severity_none;
    }



    @Override
    public void setDetail(T detail) {
        Score score = eventType.getScore(detail);
        setValue(score.getValueAtScale(severityViews.size()));
    }

    @Override
    public T getDetail() {
        return eventType.createDetail(value);
    }

    public int getValue() {
        return value;
    }
}
