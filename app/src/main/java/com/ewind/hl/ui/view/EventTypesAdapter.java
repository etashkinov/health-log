package com.ewind.hl.ui.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventType;

import java.util.List;

public class EventTypesAdapter extends RecyclerView.Adapter<EventTypesAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        EventType type;

        ViewHolder(EventButton v) {
            super(v);
            v.setOnClickListener(view -> listener.onClick(type));
        }

        void setEvent(EventType type) {
            this.type = type;
            EventButton button = (EventButton) this.itemView;
            button.setEvent(type, null);
            button.setOnEventClickListener(listener);
        }
    }

    private final List<EventType> types;
    private final EventButton.OnEventClickListener listener;

    public EventTypesAdapter(List<EventType> types, EventButton.OnEventClickListener listener) {
        this.types = types;
        this.listener = listener;
    }

    @Override
    public EventTypesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EventButton v = (EventButton) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_button, parent, false);
        v.setOrientation(LinearLayout.HORIZONTAL);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setEvent(types.get(position));
    }

    @Override
    public int getItemCount() {
        return types.size();
    }
}
