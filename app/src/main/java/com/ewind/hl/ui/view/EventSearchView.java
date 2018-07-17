package com.ewind.hl.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.EventTypeFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.ewind.hl.ui.LocalizationService.getEventTypeName;

public class EventSearchView extends LinearLayout implements SearchView.OnQueryTextListener {
    private EventButton.OnEventClickListener listener;
    private RecyclerView list;

    public EventSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnEventClickListener(EventButton.OnEventClickListener listener) {
        this.listener = listener;
        setValues(getEventTypes());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        list = findViewById(R.id.eventsList);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        SearchView eventFilter = findViewById(R.id.eventFilter);
        eventFilter.setOnQueryTextListener(this);
        eventFilter.setIconified(false);
        list.requestFocus();
    }


    @NonNull
    private List<EventType> getEventTypes() {
        return new ArrayList<>(EventTypeFactory.getAll());
    }

    private void setValues(List<EventType> types) {
        Context context = getContext();
        Collections.sort(types, (o1, o2) -> getEventTypeName(context, o1).compareTo(getEventTypeName(context, o2)));
        EventTypesAdapter adapter = new EventTypesAdapter(types, listener);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<EventType> eventTypes = getEventTypes();
        if (newText.length() > 1) {
            Iterator<EventType> iterator = eventTypes.iterator();
            while (iterator.hasNext()) {
                String name = getEventTypeName(getContext(), iterator.next()).toLowerCase();
                if (!name.contains(newText.toLowerCase())) {
                    iterator.remove();
                }
            }
        }

        setValues(eventTypes);
        return true;
    }
}
