package com.ewind.hl;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        EventActivity activity = getEventActivity();
        // Use the current date as the default date in the picker
        final Calendar c = activity.getEventDay();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(activity, this, year, month, day);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        return dialog;
    }

    private EventActivity getEventActivity() {
        return (EventActivity) getActivity();
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        getEventActivity().onDateChanged(calendar);
    }
}
