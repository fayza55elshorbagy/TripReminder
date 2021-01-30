package com.example.tripreminder.beans;

public class Trip {
    String title;
    String from;
    String to;
    String date;
    String time;
    String type;
    String status;

    public Trip(String title, String from, String to, String date, String time, String type, String status) {
        this.title = title;
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
        this.type = type;
        this.status = status;
    }

    public Trip() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
