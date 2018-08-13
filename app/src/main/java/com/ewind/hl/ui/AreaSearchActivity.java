package com.ewind.hl.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.EventTypeFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AreaSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public static final String SELECTED_AREA = "SELECTED_AREA";
    public static final String EVENT_TYPE = "EVENT_TYPE";

    private Map<String, Area> areas;

    private final class AreaViewHolder extends RecyclerView.ViewHolder {

        public AreaViewHolder(View itemView) {
            super(itemView);
        }

        public void bindTo(Area area) {
            ((TextView)itemView).setText(LocalizationService.getAreaName(area));
            itemView.setOnClickListener(v -> AreaSearchActivity.this.onAreaSelected(area));
        }
    }

    public static final DiffUtil.ItemCallback<Area> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Area>() {
                @Override
                public boolean areItemsTheSame(@NonNull Area oldArea, @NonNull Area newArea) {
                    return oldArea.getName().equals(newArea.getName());
                }
                @Override
                public boolean areContentsTheSame(@NonNull Area oldArea, @NonNull Area newArea) {
                    return areItemsTheSame(oldArea, newArea);
                }
            };

    private AreasAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initAreas();

        setContentView(R.layout.activity_area_search);

        findViewById(R.id.cancelButton).setOnClickListener(this::onCancel);

        RecyclerView list = findViewById(R.id.areaList);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        SearchView filter = findViewById(R.id.areaFilter);
        filter.setOnQueryTextListener(this);
        filter.setIconified(false);
        list.requestFocus();

        adapter = new AreasAdapter();

        list.setAdapter(adapter);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        } else {
            List<Area> recent = RecentAreasProvider.getRecent(5, this);
            adapter.submitList(recent);
        }
    }

    protected void initAreas() {
        EventType<?> type = EventTypeFactory.get(getIntent().getStringExtra(EVENT_TYPE));
        areas = new HashMap<>();
        Set<String> areaNames = EventTypeFactory.getAreas(type);
        for (String areaName : areaNames) {
            areas.put(LocalizationService.getAreaName(areaName), AreaFactory.getArea(areaName));
        }
    }

    private void onCancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void doSearch(String query) {
        if (query.length() > 2) {
            adapter.getFilter().filter(query);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        doSearch(newText);
        return true;
    }

    public void onAreaSelected(Area area) {
        RecentAreasProvider.remember(area, this);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(SELECTED_AREA, area.getName());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private class AreasAdapter extends ListAdapter<Area, AreaViewHolder> implements Filterable {
        public AreasAdapter() {
            super(AreaSearchActivity.DIFF_CALLBACK);
        }

        @NonNull
        @Override
        public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(AreaSearchActivity.this);
            View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new AreaViewHolder(view);
        }

        @Override
        public void onBindViewHolder (@NonNull AreaViewHolder holder, int position){
            holder.bindTo(getItem(position));
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String query = constraint.toString().toLowerCase();
                    FilterResults results = new FilterResults();
                    List<Area> resultValues = new LinkedList<>();
                    for (String areaName : areas.keySet()) {
                        if (areaName.toLowerCase().contains(query.trim())) {
                            resultValues.add(areas.get(areaName));
                        }
                    }
                    results.values = resultValues;
                    results.count = resultValues.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    submitList((List<Area>) results.values);
                }
            };
        }
    }
}
