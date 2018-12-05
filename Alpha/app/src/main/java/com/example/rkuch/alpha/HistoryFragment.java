package com.example.rkuch.alpha;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    private List<HistoryLogEntry> entries = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        historyAdapter = new HistoryAdapter(entries);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(historyAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HistoryLogEntry logEntry = entries.get(position);
                // TODO: Show detailed information about log entry
                Toast.makeText(getActivity(), "ENTRY: " + logEntry.getLocationText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareEntries();

        return view;
    }

    public void prepareEntries() {
        MainActivity mainActivity = (MainActivity) getActivity();
        Log.d("debug", "prepareEntries: " + mainActivity.coordinateList.size());
        for(int i = 0; i < mainActivity.coordinateList.size(); i++) {
            entries.add(new HistoryLogEntry(mainActivity.coordinateList.get(i).timeStamp, mainActivity.coordinateList.get(i).latitude + ", " + mainActivity.coordinateList.get(i).longitude));
        }

        historyAdapter.notifyDataSetChanged();
    }
}