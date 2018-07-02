package com.ewind.hl.ui.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventDate;

import java.util.Calendar;

public class EventDatePicker extends LinearLayout {
    public EventDate getDate() {
        return date;
    }

    public interface OnChangeListener {
        void onDateChanged(EventDate date);
    }

    private EventDate date;
    private OnChangeListener listener;

    public EventDatePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.event_date_picker, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        findViewById(R.id.eventDateBackButton).setOnClickListener(this::eventDateBack);
        findViewById(R.id.eventDateForwardButton).setOnClickListener(this::eventDateForward);
        findViewById(R.id.eventDatePickerButton).setOnClickListener(this::showTimePickerDialog);
    }

    public void setListener(OnChangeListener listener) {
        this.listener = listener;
    }

    @NonNull
    private EventDate getToday() {
        return new EventDate(Calendar.getInstance());
    }

    private void showTimePickerDialog(View view) {
        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(view.getContext(),
                (v,y,m,d) -> onDateChanged(new EventDate(y,m,d)),
                date.getYear(),
                date.getMonth(),
                date.getDay());
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private void eventDateBack(View view) {
        onDateChanged(date.yesterday());
    }

    private void eventDateForward(View view) {
        onDateChanged(date.tomorrow());
    }

    public void setDate(EventDate date) {
        this.date = date;
        refreshDate();
    }

    public void onDateChanged(EventDate date) {
        setDate(date);

        listener.onDateChanged(date);
    }

    private void refreshDate() {
        View eventDateForwardButton = findViewById(R.id.eventDateForwardButton);
        eventDateForwardButton.setEnabled(true);
        Button datePicker = findViewById(R.id.eventDatePickerButton);
        datePicker.setText(this.date.toString());
        EventDate today = getToday();
        if (date.equals(today)) {
            datePicker.setText(R.string.event_date_today);
            eventDateForwardButton.setEnabled(false);
        } else if (date.equals(today.yesterday())) {
            datePicker.setText(R.string.event_date_yesterday);
        }
    }
}
