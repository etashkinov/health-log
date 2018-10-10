package com.ewind.hl.ui.history;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDateComparator;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.EventTypeFactory;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.EventActionListener;
import com.ewind.hl.ui.EventAdapter;
import com.ewind.hl.ui.EventChangedListener;
import com.ewind.hl.ui.EventItemViewHolder;
import com.ewind.hl.ui.LocalizationService;
import com.ewind.hl.ui.event.EventUIFactory;
import com.ewind.hl.ui.view.area.AreaSelector;

import java.util.Collections;
import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;

public class ListHistoryActivity extends AppCompatActivity implements EventChangedListener {
    private static final String TAG = ListHistoryActivity.class.getName();
    public static final String EVENT_TYPE = "EVENT_TYPE";
    public static final String EVENT_AREA = "EVENT_AREA";
    private String type;
    private Area area;
    private EventAdapter adapter;

    /**
     * TODO remove copy&paste
     *
     * @see GraphHistoryActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        type = getIntent().getStringExtra(EVENT_TYPE);
        area = (Area) getIntent().getSerializableExtra(EVENT_AREA);

        findViewById(R.id.cancelButton).setOnClickListener(v -> finish());
        initHeader();

        initRecyclerView();

        refreshEvents();
    }

    private void refreshEvents() {
        List<Event> events = new EventsDao(this).getEvents(type, area);
        if (events.isEmpty()) {
            new EventActionListener(this).onAddNew(type);
        } else {
            Event<?> lastEvent = events.get(0);
            findViewById(R.id.addButton).setOnClickListener(v -> onAdd(lastEvent));
            adapter.setEvents(events);
        }
    }

    private void initRecyclerView() {
        RecyclerView eventsList = findViewById(R.id.eventsList);
        eventsList.setLayoutManager(new LinearLayoutManager(this));

        EventActionListener listener = new EventActionListener(this);
        adapter = new EventAdapter(new EventDateComparator()) {
            @Override
            public EventItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new HistoryEventItemViewHolder(parent, listener);
            }
        };
        adapter.setEvents(Collections.emptyList());
        eventsList.setAdapter(adapter);

        initTouchHelper(eventsList, listener);
    }

    private void initTouchHelper(RecyclerView eventsList, EventActionListener listener) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == LEFT) {
                    listener.onDelete(((EventItemViewHolder)viewHolder).getEvent());
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(eventsList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, data);
        }

        refreshEvents();
    }

    private void onAdd(Event<?> lastEvent) {
        new EventActionListener(this).onAddLike(lastEvent);
    }

    /**
     * TODO remove copy&paste
     *
     * @see com.ewind.hl.ui.EventFormActivity
     */
    private void initHeader() {
        ImageView eventImage = findViewById(R.id.eventImage);
        EventType<EventDetail> type = EventTypeFactory.get(this.type);
        Drawable drawable = EventUIFactory.getUI(type).getEventTypeDrawable(this);
        eventImage.setImageDrawable(drawable);

        TextView eventText = findViewById(R.id.eventText);
        eventText.setText(LocalizationService.getEventTypeName(this, type));

        AreaSelector areaSelector = findViewById(R.id.areaSelector);
        if (EventTypeFactory.getAreas(type).size() < 2) {
            areaSelector.setVisibility(View.GONE);
        } else {
            areaSelector.init(type, area);
        }
    }

    @Override
    public void onEventCreated(Event event) {

    }

    @Override
    public void onEventUpdated(Event event) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEventDeleted(Event event) {
        adapter.removeEvent(event);
    }
}
