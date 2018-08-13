package com.ewind.hl.ui.view.area;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.ui.AreaSearchActivity;
import com.ewind.hl.ui.LocalizationService;

import static com.ewind.hl.ui.EventActionListener.SEARCH_REQUEST_CODE;

public class AreaSelector extends LinearLayout {

    private final TextView areaLink;
    private Area area;

    public AreaSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_area_selector, this);

        areaLink = findViewById(R.id.areaLink);
    }

    public void init(EventType<?> type, Area area) {
        if (area != null) {
            setArea(area);
        } else {
            this.areaLink.setText("Where?");
            areaLink.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), AreaSearchActivity.class);
                intent.putExtra(AreaSearchActivity.EVENT_TYPE, type.getName());
                ((Activity) getContext()).startActivityForResult(intent, SEARCH_REQUEST_CODE);
            });
        }
    }

    public void setArea(Area area) {
        this.area = area;
        String areaName = LocalizationService.getAreaName(area);
        this.areaLink.setText(areaName);
    }

    public Area getArea() {
        return area;
    }
}
