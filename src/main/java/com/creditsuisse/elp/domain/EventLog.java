package com.creditsuisse.elp.domain;

public class EventLog {
    public static final int NORMAL_EVENT_MAX_PROCESSING_TIME_IN_MS = 4;
    private String id;
    private EventState state;
    private String type;
    private String host;
    private long timestamp;
    private long duration;

    public boolean isEventSlow() {
        return duration > NORMAL_EVENT_MAX_PROCESSING_TIME_IN_MS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EventState getState() {
        return state;
    }

    public void setState(EventState state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
