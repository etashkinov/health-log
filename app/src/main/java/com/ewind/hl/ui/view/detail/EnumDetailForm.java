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
import com.ewind.hl.model.event.ScoreBand;
import com.ewind.hl.model.event.ScoreEventType;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.ewind.hl.ui.view.SeverityAdapter;

public class EnumDetailForm<T extends ValueDetail> extends LinearLayout implements GenericDetailForm<T> {

    private TextView textView;
    private ScoreEventType<T> eventType;
    private SeverityAdapter adapter;

    public EnumDetailForm(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setEventType(EventType<T> eventType) {
        this.eventType = (ScoreEventType<T>) eventType;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        RecyclerView eventEnumContainer = findViewById(R.id.eventEnumContainer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(HORIZONTAL);
        eventEnumContainer.setLayoutManager(layoutManager);
        adapter = new SeverityAdapter(ScoreBand.BANDS_NUMBER, v -> updateText());
        eventEnumContainer.setAdapter(adapter);

        textView = findViewById(R.id.valueText);
        textView.setText("");
    }

    private void updateText() {
        textView.setText(eventType.getDescription(getScore(), getContext()));
    }

    @Override
    public void setDetail(T detail) {
        adapter.setValue(eventType.getScoreBand(detail).getBand());
    }

    @Override
    public T getDetail() {
        int score = getScore();
        return eventType.createDetail(score);
    }

    protected int getScore() {
        int band = adapter.getValue();
        return ScoreBand.of(band).getScore();
    }
}
