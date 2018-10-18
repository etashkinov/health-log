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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.dant.centersnapreyclerview.CenterLayoutManager;
import com.dant.centersnapreyclerview.SnappingRecyclerView;
import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDateComparator;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.ui.EventActionListener;
import com.ewind.hl.ui.EventAdapter;
import com.ewind.hl.ui.EventItemViewHolder;
import com.ewind.hl.ui.event.EventUI;
import com.ewind.hl.ui.event.EventUIFactory;
import com.ewind.hl.ui.history.period.HistoryPeriod;
import com.ewind.hl.ui.history.period.HistoryPeriodFactory.HistoryPeriodType;

import java.util.List;

public class HistoryChartFragment<D extends EventDetail> extends Fragment implements SnappingRecyclerView.SnappingRecyclerViewListener {

    private HistoryChartAdapter<D> chartAdapter;
    private EventAdapter eventAdapter;
    private TextView highLabel;
    private TextView lowLabel;
    private Spinner historyPeriodSpinner;

    public HistoryChartFragment() {
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

        highLabel = view.findViewById(R.id.chart_high_label);
        lowLabel = view.findViewById(R.id.chart_low_label);

        historyPeriodSpinner = view.findViewById(R.id.history_period_spinner);
        ArrayAdapter<HistoryPeriodType> adapter = new ArrayAdapter<>(
                container.getContext(), android.R.layout.simple_spinner_item,
                HistoryPeriodType.values());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        historyPeriodSpinner.setAdapter(adapter);
        historyPeriodSpinner.setEnabled(true);
        historyPeriodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HistoryPeriodType periodType = HistoryPeriodType.values()[position];
                chartAdapter.setItems(getHistoryActivity().getEvents(), periodType);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    public HistoryActivity<D> getHistoryActivity() {
        return (HistoryActivity<D>)getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HistoryActivity) {
            HistoryActivity<D> activity = getHistoryActivity();

            eventAdapter = new EventAdapter(new EventDateComparator()) {
                @NonNull
                @Override
                public EventItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new HistoryEventItemViewHolder(parent, new EventActionListener(activity));
                }
            };

            EventUI<D> ui = EventUIFactory.getUI(activity.getType());
            chartAdapter = new HistoryChartAdapter<>(ui, activity.getEvents(), HistoryPeriodType.DAY);
        } else {
            throw new IllegalArgumentException(HistoryActivity.class + " expected. Given: " + context.getClass());
        }
    }

    @Override
    public void onPositionChange(int position) {
        Log.i(HistoryChartFragment.class.getName(), "Position: " + position);

        chartAdapter.setPosition(position);

        TextView eventTitle = getActivity().findViewById(R.id.event_title);
        HistoryPeriod period = chartAdapter.getPeriod(position);
        eventTitle.setText(period.getLabel());

        List<Event> events = (List) chartAdapter.getEvents(position);
        eventAdapter.setEvents(events);
    }

    @Override
    public void onScroll(int dx, int dy) {
        // Do nothing
    }
}
