package com.ewind.hl.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventType;

import java.util.Arrays;

public class EventSearchView extends LinearLayout {
    public EventSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        RecyclerView list = findViewById(R.id.eventsList);
        list.setAdapter(new EventTypesAdapter(Arrays.asList(EventType.values()), t -> {

        }));
        list.setOnItemClickListener((parent,v,position,id) -> {
            dialog.cancel();
            onEventTypeChanged(EventType.values()[position]);
        });
    }
}
