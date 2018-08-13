package com.ewind.hl.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.EventTypeFactory;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.event.EventUIFactory;
import com.ewind.hl.ui.view.EventDatePicker;
import com.ewind.hl.ui.view.EventDetailForm;
import com.ewind.hl.ui.view.area.AreaSelector;

import java.util.Set;

public class EventFormActivity<D extends EventDetail> extends AppCompatActivity implements EventChangedListener {
    private static final String TAG = EventFormActivity.class.getName();

    public static final String EVENT_ID = "EVENT_ID";
    public static final String EVENT = "EVENT";

    private long id;
    private Event<D> event;
    private EventDatePicker eventDatePicker;
    private AreaSelector areaSelector;
    private EditText noteText;

    private EventDetailForm<D> detailForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);

        findViewById(R.id.cancelButton).setOnClickListener(this::onCancel);
        findViewById(R.id.okButton).setOnClickListener(this::onOk);
        findViewById(R.id.deleteButton).setOnClickListener(this::onDelete);

        eventDatePicker = findViewById(R.id.eventDatePicker);
        eventDatePicker.setListener(d -> {});

        areaSelector = findViewById(R.id.areaSelector);
        noteText = findViewById(R.id.noteText);

        id = getIntent().getLongExtra(EVENT_ID, 0L);

        initModel();
    }

    private void initModel() {
        if (id != 0) {
            event = new EventsDao(this).getEvent(id);
        } else {
            event = (Event<D>) getIntent().getSerializableExtra(EVENT);
            findViewById(R.id.deleteButton).setVisibility(View.INVISIBLE);
        }

        setEvent(event);
    }

    private void setEvent(Event<D> event) {
        initHeader(event.getType());

        eventDatePicker.setDate(event.getDate());

        areaSelector.init(event.getType(), event.getArea());

        Set<String> areas = EventTypeFactory.getAreas(event.getType());
        if (areas.size() < 2) {
            areaSelector.setVisibility(View.GONE);
        }

        initDetailForm(event);

        noteText.setText(event.getNote());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String area = data.getStringExtra(AreaSearchActivity.SELECTED_AREA);
            if (area != null) {
                areaSelector.setArea(AreaFactory.getArea(area));
            }
        }
    }

    private void onOk(View view) {
        updateEvent();
        finishOk();
    }

    private void finishOk() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EVENT_ID, id);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void onDelete(View view) {
        new EventActionListener(this).onDelete(event);
    }

    private void updateEvent() {
        new EventsDao(this).store(new Event<>(
                id,
                eventDatePicker.getDate(),
                event.getType(),
                detailForm.getDetail(),
                areaSelector.getArea() == null ? AreaFactory.getBody() : areaSelector.getArea(),
                noteText.getText().toString(),
                event.getType().getScore(detailForm.getDetail())));
    }

    private void onCancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void initHeader(EventType<D> type) {
        ImageView eventImage = findViewById(R.id.eventImage);
        Drawable drawable = EventUIFactory.getUI(type).getEventTypeDrawable(this);
        eventImage.setImageDrawable(drawable);

        TextView eventText = findViewById(R.id.eventText);
        eventText.setText(LocalizationService.getEventTypeName(this, type));
    }

    private void initDetailForm(Event<D> event) {
        ViewGroup eventDetailContainer = findViewById(R.id.eventDetailContainer);
        detailForm = EventUIFactory.getUI(event.getType()).getEventDetailForm(eventDetailContainer);
        eventDetailContainer.addView((View) detailForm);

        if (event.getDetail() != null) {
            detailForm.setDetail(event.getDetail());
        }
    }

    @Override
    public void onEventCreated(Event event) {
        // do nothing
    }

    @Override
    public void onEventUpdated(Event event) {
        // do nothing
    }

    @Override
    public void onEventDeleted(Event event) {
        finishOk();
    }
}
