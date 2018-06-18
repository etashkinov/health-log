package com.ewind.hl.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;

import java.util.List;

public class PartsAdapter extends RecyclerView.Adapter<PartsAdapter.ViewHolder> {

    interface PartSelectedListener {
        void onPartSelected(Area part);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Area part;

        ViewHolder(TextView v) {
            super(v);
            v.setOnClickListener(view -> listener.onPartSelected(part));
        }

        void setPart(Area part) {
            this.part = part;
            ((TextView)itemView).setText(part.getDescription());
        }
    }

    private final List<Area> parts;
    private final PartSelectedListener listener;

    public PartsAdapter(List<Area> parts, PartSelectedListener listener) {
        this.parts = parts;
        this.listener = listener;
    }

    @Override
    public PartsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.area_item_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setPart(parts.get(position));
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }
}
