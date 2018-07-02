package com.ewind.hl.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ewind.hl.R;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.persist.EventsDao;

import java.util.List;

public class MainActivity extends AppCompatActivity implements EventChangedListener {

    private static final String TAG = MainActivity.class.getName();
    private RecyclerView eventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AreaFactory.initBody(this); // FIXME

        setContentView(R.layout.main_activity);
        setSupportActionBar(findViewById(R.id.toolbar));

        findViewById(R.id.addNewButton).setOnClickListener(this::onEventAdd);

        eventsList = findViewById(R.id.eventsList);
//        GridLayoutManager layout = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        eventsList.setLayoutManager(layout);
        refreshEvents();
    }

    private void refreshEvents() {
        eventsList.setAdapter(new EventAdapter(getEvents(), new EventActionListener(this)));
    }

    private List<Event> getEvents() {
        return new EventsDao(this).getLatestEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings
                || super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            refreshEvents();
        }
    }

    public void onEventAdd(View view) {
        new EventActionListener(this).onAddNew();
    }

    @Override
    public void onEventCreated(Event event) {
        refreshEvents();
    }

    @Override
    public void onEventUpdated(Event event) {
        refreshEvents();
    }

    @Override
    public void onEventDeleted(Event event) {
        refreshEvents();
    }
}