package com.ewind.hl.ui.history;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.EventDateComparator;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.EventTypeFactory;
import com.ewind.hl.ui.EventActionListener;
import com.ewind.hl.ui.EventAdapter;
import com.ewind.hl.ui.EventItemViewHolder;
import com.ewind.hl.ui.view.area.AreaSelector;

import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;

public class ListHistoryFragment extends Fragment {

    private EventAdapter adapter;
    private EventActionListener listener;
    private EventType<?> type;
    private Area area;

    public ListHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.eventsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        AreaSelector areaSelector = view.findViewById(R.id.areaSelector);

        recyclerView.setAdapter(adapter);

        initTouchHelper(recyclerView, listener);

        if (EventTypeFactory.getAreas(type).size() < 2) {
            areaSelector.setVisibility(View.GONE);
        } else {
            areaSelector.init(type, area);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HistoryActivity) {
            HistoryActivity activity = (HistoryActivity) context;

            listener = new EventActionListener(activity);
            adapter = new EventAdapter(new EventDateComparator()) {
                @NonNull
                @Override
                public EventItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new HistoryEventItemViewHolder(parent, listener);
                }
            };
            adapter.setEvents(activity.getEvents());

            type = activity.type;
            area = activity.area;
        } else {
            throw new IllegalArgumentException(HistoryActivity.class + " expected. Given: " + context.getClass());
        }
    }

    private void initTouchHelper(RecyclerView eventsList, EventActionListener listener) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == LEFT) {
                    listener.onDelete(((EventItemViewHolder)viewHolder).getEvent());
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(eventsList);
    }
}
