package com.ewind.hl.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ewind.hl.R;
import com.ewind.hl.ui.EventUI;

import java.util.ArrayList;
import java.util.List;

public class SeverityAdapter extends RecyclerView.Adapter<SeverityAdapter.SeverityViewHolder> {

    private final int size;
    private final List<ImageView> views;
    private final View.OnClickListener listener;

    private int value;

    public SeverityAdapter(int size, View.OnClickListener listener) {
        this.size = size;
        this.views = new ArrayList<>(size);
        this.listener = listener;
    }

    public static class SeverityViewHolder extends RecyclerView.ViewHolder {
        public SeverityViewHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public SeverityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ImageView view = (ImageView) inflater.inflate(R.layout.view_severity, parent, false);
        SeverityViewHolder holder = new SeverityViewHolder(view);
        view.setOnClickListener(v -> setValue(holder.getAdapterPosition(), context));
        views.add(view);
        return holder;
    }

    public void setValue(int value, Context context) {
        this.value = value;

        for (int i = 0; i < size; i++) {
            int drawableId = getDrawableId(value, i, context);
            views.get(i).setImageDrawable(context.getDrawable(drawableId));
        }

        listener.onClick(null);
    }

    public int getValue() {
        return value;
    }

    private int getDrawableId(int value, int imageViewIndex, Context context) {
        int valueDrawable = EventUI.getDrawableByName("ic_severity_" + value, context);
        return imageViewIndex <= value ? valueDrawable : R.drawable.ic_severity_none;
    }

    @Override
    public void onBindViewHolder(@NonNull SeverityViewHolder holder, int position) {
        // do nothing
    }

    @Override
    public int getItemCount() {
        return size;
    }
}
