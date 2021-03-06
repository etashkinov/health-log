package com.ewind.hl.ui.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.DayPart;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.ui.LocalizationService;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class EventDatePicker extends ConstraintLayout {

    private LocalDate localDate;
    private DayPart dayPart;

    private Spinner eventDayPartSpinner;
    private OnChangeListener listener;

    public EventDatePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.event_date_picker, this);
    }

    public EventDate getDate() {
        return new EventDate(localDate, dayPart);
    }

    public void setDate(EventDate date) {
        localDate = date.getLocalDate();
        dayPart = date.getDayPart();

        refreshDate();
    }

    private int getDayPartSpinnerIndex() {
        return dayPart.ordinal();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        eventDayPartSpinner = findViewById(R.id.eventDayPartSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item,
                getLocalizedDayParts());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventDayPartSpinner.setAdapter(adapter);
        eventDayPartSpinner.setEnabled(false);
        eventDayPartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DayPart dayPart = DayPart.values()[position];
                if (dayPart != EventDatePicker.this.dayPart) {
                    onDayPartChanged(dayPart);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @NonNull
    private List<String> getLocalizedDayParts() {
        DayPart[] dayParts = DayPart.values();
        List<String> result = new ArrayList<>(dayParts.length);
        for (DayPart part : dayParts) {
            result.add(LocalizationService.getDayPart(part));
        }
        return result;
    }

    private void onDayPartChanged(DayPart dayPart) {
        EventDatePicker.this.dayPart = dayPart;

        notifyDateChanged();
    }

    public void setListener(OnChangeListener listener) {
        this.listener = listener;

        eventDayPartSpinner.setEnabled(true);
        findViewById(R.id.eventDatePickerButton).setOnClickListener(this::showTimePickerDialog);
    }

    private void showTimePickerDialog(View view) {
        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(view.getContext(),
                (v,y,m,d) -> onDateChanged(y,m,d),
                localDate.getYear(),
                localDate.getMonthOfYear() - 1,
                localDate.getDayOfMonth());
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private void onDateChanged(int year, int month, int day) {
        localDate = localDate
                        .withYear(year)
                        .withMonthOfYear(month + 1)
                        .withDayOfMonth(day);

        notifyDateChanged();
    }

    private void notifyDateChanged() {
        refreshDate();
        listener.onDateChanged(getDate());
    }

    private void refreshDate() {
        TextView datePicker = findViewById(R.id.eventDatePickerButton);
        datePicker.setText(LocalizationService.getLocalDate(localDate));
        LocalDate today = LocalDate.now();
        if (localDate.equals(today)) {
            datePicker.setText(R.string.event_date_today);
        } else if (localDate.equals(today.minusDays(1))) {
            datePicker.setText(R.string.event_date_yesterday);
        }

        eventDayPartSpinner.setSelection(getDayPartSpinnerIndex());
    }

    public interface OnChangeListener {
        void onDateChanged(EventDate date);
    }
}
