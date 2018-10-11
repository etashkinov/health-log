package com.ewind.hl.ui.history;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dant.centersnapreyclerview.CenterLayoutManager;
import com.dant.centersnapreyclerview.SnappingRecyclerView;
import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDateComparator;
import com.ewind.hl.ui.EventActionListener;
import com.ewind.hl.ui.EventAdapter;
import com.ewind.hl.ui.EventItemViewHolder;

import java.util.List;

public class ChartHistoryFragment extends Fragment implements SnappingRecyclerView.SnappingRecyclerViewListener {

    private HistoryChartAdapter chartAdapter;
    private EventAdapter eventAdapter;

    public ChartHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_chart, container, false);
        SnappingRecyclerView recyclerView = view.findViewById(R.id.history_chart);

        ((CenterLayoutManager)recyclerView.getLayoutManager()).setStackFromEnd(true);
        recyclerView.setListener(this);
        recyclerView.setAdapter(chartAdapter);

        RecyclerView eventsList = view.findViewById(R.id.events_list);
        eventsList.setAdapter(eventAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HistoryActivity) {
            HistoryActivity activity = (HistoryActivity) context;

            eventAdapter = new EventAdapter(new EventDateComparator()) {
                @NonNull
                @Override
                public EventItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new HistoryEventItemViewHolder(parent, new EventActionListener(activity));
                }
            };

            chartAdapter = new HistoryChartAdapter(activity.getEvents());
        } else {
            throw new IllegalArgumentException(HistoryActivity.class + " expected. Given: " + context.getClass());
        }
    }

    @Override
    public void onPositionChange(int position) {
        Log.i(ChartHistoryFragment.class.getName(), "Position: " + position);
        TextView eventTitle = getActivity().findViewById(R.id.event_title);
        chartAdapter.setPosition(position);
        eventTitle.setText(chartAdapter.getDate(position).toString());

        List<Event> events = chartAdapter.getEvents(position);
        eventAdapter.setEvents(events);
    }

    @Override
    public void onScroll(int dx, int dy) {
        // Do nothing
    }
}
