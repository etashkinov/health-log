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
import com.ewind.hl.persist.EventsDao;

import java.util.Calendar;
import java.util.List;

public class EventsViewController {

    private EventDate date;
    private final RelativeLayout eventsView;
    private final AreaViewController areaController;

    public EventsViewController(EventActivity activity) {
        this.eventsView = activity.findViewById(R.id.eventsView);
        this.areaController = new AreaViewController(activity);

        onDateChanged(getToday());

        eventsView.findViewById(R.id.eventDateBackButton).setOnClickListener(this::eventDateBack);
        eventsView.findViewById(R.id.eventDatePickerButton).setOnClickListener(v -> showTimePickerDialog(activity));
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

        refreshEvents();
    }

    public void refreshEvents() {
        areaController.setEvents(findEvents());
    }

    public EventDate getDate() {
        return date;
    }

    private void showTimePickerDialog(EventActivity activity) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(activity.getSupportFragmentManager(), "datePicker");
    }

    private void eventDateBack(View view) {
        onDateChanged(date.yesterday());
    }

    private void eventDateForward(View view) {
        onDateChanged(date.tomorrow());
    }

    @NonNull
    private List<Event> findEvents() {
        return EventsDao.getEvents(date);
    }
}
