package com.ewind.hl.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewind.hl.R;

public class ChartHistoryFragment extends Fragment {

    private HistoryChartAdapter adapter;

    public ChartHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_chart, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.history_chart);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HistoryActivity) {
            HistoryActivity activity = (HistoryActivity) context;

            adapter = new HistoryChartAdapter(activity.getEvents());
        } else {
            throw new IllegalArgumentException(HistoryActivity.class + " expected. Given: " + context.getClass());
        }
    }
}
