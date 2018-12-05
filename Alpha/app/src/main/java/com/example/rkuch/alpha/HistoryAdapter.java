package com.example.rkuch.alpha;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.security.Timestamp;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<HistoryLogEntry> logEntries;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView locationText, timestamp;

        public ViewHolder(View view) {
            super(view);
            locationText = view.findViewById(R.id.location_text);
            timestamp = view.findViewById(R.id.log_timestamp);
        }
    }

    public HistoryAdapter(List<HistoryLogEntry> logEntries) {
        this.logEntries = logEntries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        HistoryLogEntry entry = logEntries.get(position);
        Date date = new Date(entry.getTimestamp());
        viewHolder.timestamp.setText(date.toString());
        viewHolder.locationText.setText(entry.getLocationText());
    }

    @Override
    public int getItemCount() {
        return logEntries.size();
    }
}


