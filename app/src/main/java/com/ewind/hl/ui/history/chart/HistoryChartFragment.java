package com.ewind.hl.ui.history.chart;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.ewind.hl.ui.history.EventDeleteTouchCallback;
import com.ewind.hl.ui.history.HistoryActivity;
import com.ewind.hl.ui.history.HistoryEventItemViewHolder;
import com.ewind.hl.ui.history.chart.period.HistoryPeriod;
import com.ewind.hl.ui.history.chart.period.HistoryPeriodFactory;
import com.ewind.hl.ui.history.chart.period.HistoryPeriodFactory.HistoryPeriodType;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HistoryChartFragment<D extends EventDetail> extends Fragment implements SnappingRecyclerView.SnappingRecyclerViewListener {

    private HistoryChartAdapter<D> chartAdapter;
    private EventAdapter eventAdapter;
    private TextView highLabel;
    private TextView lowLabel;
    private Map<HistoryPeriod, List<Event<D>>> grouping;
    private TextView eventTitle;
    private View addButton;

    public HistoryChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_chart, container, false);
        SnappingRecyclerView recyclerView = view.findViewById(R.id.history_chart);

        HistoryActivity<D> activity = getHistoryActivity();

        eventAdapter = new EventAdapter(new EventDateComparator()) {
            @NonNull
            @Override
            public EventItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new HistoryEventItemViewHolder(parent, new EventActionListener(activity));
            }
        };

        RecyclerView eventsList = view.findViewById(R.id.events_list);
        eventsList.setAdapter(eventAdapter);
        EventDeleteTouchCallback.attach(eventsList, activity);

        EventUI<D> ui = EventUIFactory.getUI(activity.getType(), getActivity());

        chartAdapter = new HistoryChartAdapter(ui);

        ((CenterLayoutManager)recyclerView.getLayoutManager()).setStackFromEnd(true);
        recyclerView.setListener(this);
        recyclerView.setAdapter(chartAdapter);

        highLabel = view.findViewById(R.id.chart_high_label);
        lowLabel = view.findViewById(R.id.chart_low_label);
        eventTitle = view.findViewById(R.id.event_title);
        addButton = activity.findViewById(R.id.addButton);

        Spinner historyPeriodSpinner = view.findViewById(R.id.history_period_spinner);
        Context context = container.getContext();
        initSpinner(historyPeriodSpinner, context, ui);

        updateEvents(activity.getEvents(), HistoryPeriodType.DAY, ui);

        return view;
    }

    protected void initSpinner(Spinner historyPeriodSpinner, Context context, EventUI<D> ui) {
        ArrayAdapter<HistoryPeriodType> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_item,
                HistoryPeriodType.values());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        historyPeriodSpinner.setAdapter(adapter);
        historyPeriodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HistoryPeriodType periodType = HistoryPeriodType.values()[position];
                List<Event<D>> events = getHistoryActivity().getEvents();
                updateEvents(events, periodType, ui);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    protected void updateEvents(List<Event<D>> events, HistoryPeriodType periodType, EventUI<D> ui) {
        ChartData chartData = groupEvents(ui, periodType, events);
        lowLabel.setText(chartData.getLowLabel());
        highLabel.setText(chartData.getHighLabel());
        chartAdapter.setItems(chartData.getItems());
        chartAdapter.scrollToPeriod(HistoryPeriodFactory.toPeriod(LocalDate.now(), periodType));
    }

    protected ChartData groupEvents(EventUI<D> ui, HistoryPeriodType periodType, List<Event<D>> events) {
        grouping = groupByPeriod(events, periodType);

        HistoryPeriod today = HistoryPeriodFactory.toPeriod(LocalDate.now(), periodType);
        HistoryPeriod from = grouping.isEmpty() ? today : Collections.min(grouping.keySet()).add(-10);
        HistoryPeriod till = today.add(10);
        return ui.toChartData(grouping, from, till);
    }

    public HistoryActivity<D> getHistoryActivity() {
        return (HistoryActivity<D>)getActivity();
    }

    @NonNull
    private Map<HistoryPeriod, List<Event<D>>> groupByPeriod(List<Event<D>> events, HistoryPeriodType periodType) {
        Map<HistoryPeriod, List<Event<D>>> grouping = new HashMap<>();
        for (Event<D> event : events) {
            HistoryPeriod period = HistoryPeriodFactory.toPeriod(event.getDate().getLocalDate(), periodType);
            List<Event<D>> periodEvents = grouping.get(period);
            if (periodEvents == null) {
                periodEvents = new LinkedList<>();
                grouping.put(period, periodEvents);
            }

            periodEvents.add(event);
        }
        return grouping;
    }

    @Override
    public void onPositionChange(int position) {
        chartAdapter.onPositionChanged(position);
        HistoryPeriod period = chartAdapter.getPeriod(position);

        eventTitle.setText(period.getLabel());

        List events = grouping.get(period);
        eventAdapter.setEventItems(events, getActivity());
    }

    @Override
    public void onScroll(int dx, int dy) {
        // Do nothing
    }
}
