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
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.view.EventDatePicker;
import com.ewind.hl.ui.view.EventDetailForm;
import com.ewind.hl.ui.view.area.AreaSelector;

public class EventFormActivity extends AppCompatActivity implements EventChangedListener {
    private static final String TAG = EventFormActivity.class.getName();

    public static final String EVENT_ID = "EVENT_ID";
    public static final String EVENT = "EVENT";

    private long id;
    private Event event;
    private EventDatePicker eventDatePicker;
    private AreaSelector areaSelector;
    private EditText noteText;

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
            event = (Event) getIntent().getSerializableExtra(EVENT);
            findViewById(R.id.deleteButton).setVisibility(View.INVISIBLE);
        }

        setEvent(event);
    }

    private void setEvent(Event event) {
        initHeader(event.getType());

        eventDatePicker.setDate(event.getDate());
        areaSelector.setArea(event.getType(), event.getArea());

        initDetailForm(event);

        noteText.setText(event.getNote());
    }

    private EventDetailForm detailForm;

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
        new EventsDao(this).store(new Event(
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

    private void initHeader(EventType type) {
        ImageView eventImage = findViewById(R.id.eventImage);
        Drawable drawable = EventUI.getEventTypeDrawable(type, this);
        eventImage.setImageDrawable(drawable);

        TextView eventText = findViewById(R.id.eventText);
        eventText.setText(LocalizationService.getEventTypeName(this, type));
    }

    private void initDetailForm(Event event) {
        detailForm = EventUI.getEventDetailForm(event, this);
        ((ViewGroup) findViewById(R.id.eventDetailContainer)).addView((View) detailForm);

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
