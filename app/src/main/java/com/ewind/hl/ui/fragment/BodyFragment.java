package com.ewind.hl.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewind.hl.MainActivity;
import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;

public class BodyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.body_view, null);
        RecyclerView partsList = initPartsList(view);
        MainActivity activity = (MainActivity) getActivity();
        partsList.setAdapter(new PartsAdapter(activity.getState().getArea().getParts(), this::onAreaChanged));
        return view;
    }

    private RecyclerView initPartsList(View view) {
        RecyclerView result = view.findViewById(R.id.partsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        result.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                result.getContext(),
                layoutManager.getOrientation());
        result.addItemDecoration(dividerItemDecoration);
        return result;
    }

    private void onAreaChanged(Area area) {
        ((MainActivity)getActivity()).onAreaChanged(area);
    }


}
