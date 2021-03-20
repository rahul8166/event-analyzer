package com.test.model;

public class EventEntity {
    private String eventId;
    private String type;
    private String host;
    private Long duration;
    private Boolean alert;

    public EventEntity(String eventId, String type, String host, Long duration, Boolean alert) {
        this.eventId = eventId;
        this.type = type;
        this.host = host;
        this.duration = duration;
        this.alert = alert;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Boolean getAlert() {
        return alert;
    }

    public void setAlert(Boolean alert) {
        this.alert = alert;
    }
}
