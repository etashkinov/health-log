package com.ewind.hl.ui.view.body;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;

import java.util.List;

public class AreaSelector extends ConstraintLayout {

    private final AutoCompleteTextView areaAutoComplete;

    private Area area;

    public AreaSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_area_selector, this);

        areaAutoComplete = findViewById(R.id.areaAutoComplete);
        List<Area> areas = AreaFactory.getAreas();
        ArrayAdapter<Area> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, areas);
        areaAutoComplete.setAdapter(adapter);
        areaAutoComplete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area = areas.get(position);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setArea(Area area) {
        this.area = area;

        if (area != null) {
            areaAutoComplete.setText(area.toString());
        }
    }

    public Area getArea() {
        return area;
    }
}
