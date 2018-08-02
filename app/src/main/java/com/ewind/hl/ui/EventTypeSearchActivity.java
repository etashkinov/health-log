package com.ewind.hl.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.EventTypeFactory;
import com.ewind.hl.ui.view.EventTypesAdapter;

import java.util.List;

public class EventTypeSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, EventTypeSelectedListener {
    public static final String SELECTED_EVENT_TYPE = "SELECTED_EVENT_TYPE";

    private EventTypesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_type_search);

        findViewById(R.id.cancelButton).setOnClickListener(this::onCancel);

        RecyclerView list = findViewById(R.id.eventsList);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        SearchView eventFilter = findViewById(R.id.eventFilter);
        eventFilter.setOnQueryTextListener(this);
        eventFilter.setIconified(false);
        list.requestFocus();

        adapter = new EventTypesAdapter(EventTypeFactory.getAll(), this, this);
        list.setAdapter(adapter);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        } else {
            List<EventType<?>> recent = RecentEventTypesProvider.getRecent(5, this);
            adapter.setEventTypes(recent, this);
        }
    }

    private void onCancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void doSearch(String query) {
        if (query.length() > 2) {
            adapter.getFilter().filter(query);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        doSearch(newText);
        return true;
    }

    @Override
    public void onEventTypeSelected(EventType<?> type) {
        RecentEventTypesProvider.remember(type, this);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(SELECTED_EVENT_TYPE, type.getName());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
