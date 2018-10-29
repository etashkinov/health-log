package com.ewind.hl.ui.history;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventDateComparator;
import com.ewind.hl.ui.EventActionListener;
import com.ewind.hl.ui.EventAdapter;
import com.ewind.hl.ui.EventItemViewHolder;

public class HistoryListFragment extends Fragment {

    public HistoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.eventsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        HistoryActivity activity = (HistoryActivity) getActivity();
        EventAdapter adapter = new EventAdapter(new EventDateComparator()) {
            @NonNull
            @Override
            public EventItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new HistoryEventItemViewHolder(parent, new EventActionListener(activity));
            }
        };
        adapter.setEventItems(activity.getEvents(), activity);

        recyclerView.setAdapter(adapter);

        EventDeleteTouchCallback.attach(recyclerView, activity);

        return view;
    }
}
