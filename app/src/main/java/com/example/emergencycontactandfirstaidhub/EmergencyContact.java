package com.example.emergencycontactandfirstaidhub;

public class EmergencyContact {
    public int id;
    public String name;
    public String phone;
    public String type;
    public int isPriority;

    public EmergencyContact(String name, String phone, String type) {
        this.name = name;
        this.phone = phone;
        this.type = type;
        this.isPriority = 0;
    }

    public EmergencyContact(String name, String phone, String type, int isPriority) {
        this.name = name;
        this.phone = phone;
        this.type = type;
        this.isPriority = isPriority;
    }

    public EmergencyContact(int id, String name, String phone, String type) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.type = type;
        this.isPriority = 0;
    }

    public EmergencyContact(int id, String name, String phone, String type, int isPriority) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.type = type;
        this.isPriority = isPriority;
    }
}
