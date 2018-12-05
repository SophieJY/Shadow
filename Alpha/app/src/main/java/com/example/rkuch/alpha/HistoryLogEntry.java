package com.example.rkuch.alpha;

// TODO: Use double for latitude and longitude
public class HistoryLogEntry {
    private long timestamp;
    private String locationText;

    public HistoryLogEntry(long timestamp, String locationText) {
        this.timestamp = timestamp;
        this.locationText = locationText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getLocationText() {
        return locationText;
    }
}
