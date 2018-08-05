package com.ewind.hl.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ewind.hl.R;

public class SeverityAdapter extends RecyclerView.Adapter<SeverityAdapter.SeverityViewHolder> {

    private final int size;
    private final View.OnClickListener listener;

    private int value;

    public SeverityAdapter(int size, View.OnClickListener listener) {
        this.size = size;
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
        view.setOnClickListener(v -> setValue(holder.getAdapterPosition()));
        return holder;
    }

    public void setValue(int value) {
        this.value = value;
        listener.onClick(null);
        notifyDataSetChanged();
    }

    public int getValue() {
        return value;
    }

    private int getDrawableId(int value, int imageViewIndex, Context context) {
        int valueDrawable = context.getResources().getIdentifier("ic_severity_" + value, "drawable",
                context.getPackageName());
        return imageViewIndex <= value ? valueDrawable : R.drawable.ic_severity_none;
    }

    @Override
    public void onBindViewHolder(@NonNull SeverityViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        int drawableId = getDrawableId(value, position, context);
        ((ImageView)holder.itemView).setImageDrawable(context.getDrawable(drawableId));
    }

    @Override
    public int getItemCount() {
        return size;
    }
}
