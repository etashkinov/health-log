package com.ewind.hl;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.ewind.hl.model.event.EventDate;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        EventActivity activity = getEventActivity();
        // Use the current date as the default date in the picker
        final EventDate date = activity.getEventDay();

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(activity, this,
                date.getYear(),
                date.getMonth(),
                date.getDay());
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        return dialog;
    }

    private EventActivity getEventActivity() {
        return (EventActivity) getActivity();
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        getEventActivity().onDateChanged(new EventDate(year, month, day));
    }
}
