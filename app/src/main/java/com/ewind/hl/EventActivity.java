package com.ewind.hl;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class EventActivity extends AppCompatActivity {

    private Calendar eventDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.moodButton);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());

        onDateChanged(Calendar.getInstance());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onDateChanged(Calendar day) {
        eventDay = day;

        Button eventDateForwardButton = findViewById(R.id.eventDateForwardButton);
        eventDateForwardButton.setEnabled(true);

        Button datePicker = findViewById(R.id.eventDatePickerButton);

        datePicker.setText(DateFormat.format("dd/MM/yyyy", eventDay));

        Calendar today = Calendar.getInstance();
        if (eventDay.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && eventDay.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
            int dayOfMonth = eventDay.get(Calendar.DAY_OF_MONTH);
            if (dayOfMonth == today.get(Calendar.DAY_OF_MONTH)) {
                datePicker.setText(R.string.event_date_today);
                eventDateForwardButton.setEnabled(false);
            } else if (dayOfMonth == today.get(Calendar.DAY_OF_MONTH) - 1) {
                datePicker.setText(R.string.event_date_yesterday);
            }
        }
    }

    public void eventDateBack(View view) {
        eventDay.add(Calendar.DAY_OF_MONTH, -1);
        onDateChanged(eventDay);
    }

    public void eventDateForward(View view) {
        eventDay.add(Calendar.DAY_OF_MONTH, 1);
        onDateChanged(eventDay);
    }

    public Calendar getEventDay() {
        return eventDay;
    }
}
