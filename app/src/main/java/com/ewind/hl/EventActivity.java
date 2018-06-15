package com.ewind.hl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ewind.hl.controller.EventsViewController;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.persist.EventsDao;

public class EventActivity extends AppCompatActivity {

    private EventsViewController eventsViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventsViewController = new EventsViewController(this);
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

    public EventDate getEventDay() {
        return eventsViewController.getDate();
    }

    public void onDateChanged(EventDate eventDate) {
        eventsViewController.onDateChanged(eventDate);
    }

    public void onEventSubmit(Area area, EventDetail detail) {
        EventDate date = eventsViewController.getDate();
        Event<?> event = new Event<>(detail, date, area, null);
        EventsDao.store(event);
        eventsViewController.refreshEvents();
    }
}
