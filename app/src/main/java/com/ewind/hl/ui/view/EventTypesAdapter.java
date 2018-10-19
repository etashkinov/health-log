package com.ewind.hl.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.ui.EventTypeSelectedListener;
import com.ewind.hl.ui.event.EventUIFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import static com.ewind.hl.ui.LocalizationService.getEventTypeName;

public class EventTypesAdapter extends RecyclerView.Adapter<EventTypesAdapter.ViewHolder> implements Filterable {

    private final TreeMap<String, EventType> types;
    private List<String> filteredValues;

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View v) {
            super(v);
        }
    }

    private final EventTypeSelectedListener listener;

    public EventTypesAdapter(Collection<EventType> types, EventTypeSelectedListener listener, Context context) {
        this.listener = listener;
        this.types = getEventTypeLabelsMap(types, context);
        this.filteredValues = new ArrayList<>(0);
    }

    @NonNull
    private TreeMap<String, EventType> getEventTypeLabelsMap(Collection<EventType> all, Context context) {
        TreeMap<String, EventType> types = new TreeMap<>();
        for (EventType type : all) {
            types.put(getEventTypeLabel(type, context), type);
        }
        return types;
    }

    private String getEventTypeLabel(EventType type, Context context) {
        return getEventTypeName(context, type);
    }

    @Override
    public EventTypesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.event_button, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();
                List<String> resultValues = new LinkedList<>();
                for (String type : types.navigableKeySet()) {
                    if (type.toLowerCase().contains(query)) {
                        resultValues.add(type);
                    }
                }
                results.values = resultValues;
                results.count = resultValues.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                setValues((List<String>) results.values);
            }
        };
    }

    public void setEventTypes(List<EventType<?>> types, Context context) {
        List<String> values = new ArrayList<>(types.size());
        for (EventType<?> result : types) {
            values.add(getEventTypeLabel(result, context));
        }
        setValues(values);
    }

    private void setValues(List<String> results) {
        filteredValues = results;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String eventLabel = filteredValues.get(position);
        EventType type = types.get(eventLabel);
        View view = holder.itemView;

        view.setOnClickListener(v -> listener.onEventTypeSelected(type));
        TextView eventText = view.findViewById(R.id.eventText);
        eventText.setText(eventLabel);

        ImageView eventImage = view.findViewById(R.id.eventImage);
        Drawable eventDrawable = EventUIFactory.getUI(type, view.getContext())
                .getEventTypeDrawable(view.getContext());
        eventImage.setImageDrawable(eventDrawable);
    }

    @Override
    public int getItemCount() {
        return filteredValues.size();
    }
}
