package com.ewind.hl.ui.view.area;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.ui.AreaSearchActivity;
import com.ewind.hl.ui.LocalizationService;

import static com.ewind.hl.ui.EventActionListener.SEARCH_REQUEST_CODE;

public class AreaSelector extends ConstraintLayout {

    private final TextView areaLink;
    private Area area;


    public AreaSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_area_selector, this);

        areaLink = findViewById(R.id.areaLink);
    }

    public void setArea(EventType<?> type, Area area) {
        this.area = area;

        if (area != null) {
            String areaName = LocalizationService.getAreaName(area);
            this.areaLink.setText(areaName);
        } else {
            this.areaLink.setText("Where?");
        }
        areaLink.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AreaSearchActivity.class);
            intent.putExtra(AreaSearchActivity.EVENT_TYPE, type.getName());
            ((Activity) getContext()).startActivityForResult(intent, SEARCH_REQUEST_CODE);
        });
    }

    public Area getArea() {
        return area;
    }
}
