package com.example.rkuch.alpha;

// TODO: Use double for latitude and longitude
public class HistoryLogEntry {
    private int timestamp;
    private String locationText;

    public HistoryLogEntry(int timestamp, String locationText) {
        this.timestamp = timestamp;
        this.locationText = locationText;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getLocationText() {
        return locationText;
    }
}
