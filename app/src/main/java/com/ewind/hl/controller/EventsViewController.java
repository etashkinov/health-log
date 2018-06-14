package com.ewind.hl.controller;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ewind.hl.DatePickerFragment;
import com.ewind.hl.EventActivity;
import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class EventsViewController {

    private EventDate date;
    private final RelativeLayout eventsView;
    private final AreaViewController areaController;

    public EventsViewController(RelativeLayout eventsView) {
        this.eventsView = eventsView;
        this.areaController = new AreaViewController(eventsView.findViewById(R.id.areaView));

        onDateChanged(getToday());

        eventsView.findViewById(R.id.eventDateBackButton).setOnClickListener(this::eventDateBack);
        eventsView.findViewById(R.id.eventDatePickerButton).setOnClickListener(this::showTimePickerDialog);
        eventsView.findViewById(R.id.eventDateForwardButton).setOnClickListener(this::eventDateForward);
    }

    @NonNull
    private EventDate getToday() {
        return EventDate.of(Calendar.getInstance());
    }
    public void onDateChanged(EventDate date) {
        this.date = date;

        Button eventDateForwardButton = eventsView.findViewById(R.id.eventDateForwardButton);
        eventDateForwardButton.setEnabled(true);

        Button datePicker = eventsView.findViewById(R.id.eventDatePickerButton);

        datePicker.setText(this.date.toString());

        EventDate today = getToday();
        if (date.equals(today)) {
            datePicker.setText(R.string.event_date_today);
            eventDateForwardButton.setEnabled(false);
        } else if (date.equals(today.yesterday())) {
            datePicker.setText(R.string.event_date_yesterday);
        }

        areaController.setEvents(findEvents());
    }

    public EventDate getDate() {
        return date;
    }

    private void showTimePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(((EventActivity)view.getContext()).getSupportFragmentManager(), "datePicker");
    }

    private void eventDateBack(View view) {
        onDateChanged(date.yesterday());
    }

    private void eventDateForward(View view) {
        onDateChanged(date.tomorrow());
    }

    @NonNull
    private List<Event> findEvents() {
        return Collections.emptyList();
    }
}
