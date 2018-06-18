package com.ewind.hl.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.detail.MoodDetail;
import com.ewind.hl.ui.view.EventDetailForm;

public class MoodDetailForm extends LinearLayout implements EventDetailForm<MoodDetail>, SeekBar.OnSeekBarChangeListener {

    private SeekBar seekBar;
    private TextView textView;

    public MoodDetailForm(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        seekBar = findViewById(R.id.moodSeekBar);
        seekBar.setMax(MoodDetail.values().length - 1);

        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(0);

        textView = findViewById(R.id.moodText);
        textView.setText("");
    }

    @Override
    public void setDetail(MoodDetail detail) {
        seekBar.setProgress(detail.ordinal());
    }

    @Override
    public MoodDetail getDetail() {
        return MoodDetail.values()[seekBar.getProgress()];
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textView.setText(MoodDetail.values()[progress].name());
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
