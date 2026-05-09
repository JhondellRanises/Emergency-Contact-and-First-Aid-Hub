package com.example.emergencycontactandfirstaidhub;

public class AlertItem {
    public int id;
    public String title;
    public String message;
    public long whenMillis;
    public String type;

    public AlertItem(String title, String message, long whenMillis, String type) {
        this.title = title;
        this.message = message;
        this.whenMillis = whenMillis;
        this.type = type;
    }

    public AlertItem(int id, String title, String message, long whenMillis, String type) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.whenMillis = whenMillis;
        this.type = type;
    }
}
