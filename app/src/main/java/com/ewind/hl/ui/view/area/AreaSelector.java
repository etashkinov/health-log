package com.ewind.hl.ui.view.area;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.ui.LocalizationService;

import java.util.List;

public class AreaSelector extends ConstraintLayout {

    private final Spinner areaSpinner;

    private List<Area> values;

    public AreaSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_area_selector, this);

        areaSpinner = findViewById(R.id.areaSpinner);
    }

    public void setArea(EventType type, Area area) {
        values = AreaFactory.getAreas(type);

        ArrayAdapter<Area> adapter = new ArrayAdapter<Area>(getContext(), android.R.layout.simple_spinner_item, values) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);

                view.setText(LocalizationService.getAreaName(getItem(position)));

                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(adapter);

        if (area != null) {
            areaSpinner.setSelection(values.indexOf(area));
        }

        if (values.size() <= 1) {
            setVisibility(GONE);
            areaSpinner.setSelection(0);
        }
    }

    public Area getArea() {
        return (Area) areaSpinner.getSelectedItem();
    }
}
