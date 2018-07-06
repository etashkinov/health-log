package com.ewind.hl.ui.view.area;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.ui.LocalizationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaSelector extends ConstraintLayout {

    private final Spinner areaSpinner;

    private Map<String, Area> values;

    public AreaSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_area_selector, this);

        areaSpinner = findViewById(R.id.areaSpinner);
    }

    public void setArea(EventType type, Area area) {
        values = new HashMap<>();
        List<Area> areas = AreaFactory.getAreas(type);
        for (Area value : areas) {
            values.put(getAreaName(value), value);
        }

        ArrayList<String> strings = new ArrayList<>(values.keySet());
        Collections.sort(strings);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(adapter);

        if (area != null) {
            areaSpinner.setSelection(strings.indexOf(getAreaName(area)));
        }

        if (values.size() <= 1) {
            setVisibility(GONE);
            areaSpinner.setSelection(0);
        }
    }

    @NonNull
    private String getAreaName(Area value) {
        return LocalizationService.getAreaName(value);
    }

    public Area getArea() {
        String item = (String) areaSpinner.getSelectedItem();
        return values.get(item);
    }
}
