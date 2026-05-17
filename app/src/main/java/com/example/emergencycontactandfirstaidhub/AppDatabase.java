package com.example.emergencycontactandfirstaidhub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AppDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "safety_hub.db";
    private static final int DB_VERSION = 2;

    public AppDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contacts(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT, type TEXT, is_priority INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE alerts(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, message TEXT, when_millis INTEGER, type TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        db.execSQL("DROP TABLE IF EXISTS alerts");
        onCreate(db);
    }

    public long insertContact(EmergencyContact contact) {
        ContentValues cv = new ContentValues();
        cv.put("name", contact.name);
        cv.put("phone", contact.phone);
        cv.put("type", contact.type);
        cv.put("is_priority", contact.isPriority);
        return getWritableDatabase().insert("contacts", null, cv);
    }

    public int updateContact(EmergencyContact contact) {
        ContentValues cv = new ContentValues();
        cv.put("name", contact.name);
        cv.put("phone", contact.phone);
        cv.put("type", contact.type);
        cv.put("is_priority", contact.isPriority);
        return getWritableDatabase().update("contacts", cv, "id=?", new String[]{String.valueOf(contact.id)});
    }

    public int updatePriority(int id, int isPriority) {
        ContentValues cv = new ContentValues();
        cv.put("is_priority", isPriority);
        return getWritableDatabase().update("contacts", cv, "id=?", new String[]{String.valueOf(id)});
    }

    public int deleteContact(int id) {
        return getWritableDatabase().delete("contacts", "id=?", new String[]{String.valueOf(id)});
    }

    public List<EmergencyContact> getAllContacts(String query) {
        List<EmergencyContact> items = new ArrayList<>();
        String q = "%" + query + "%";
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id,name,phone,type,is_priority FROM contacts WHERE name LIKE ? OR phone LIKE ? OR type LIKE ? ORDER BY is_priority DESC, name ASC",
                new String[]{q, q, q}
        );
        while (c.moveToNext()) {
            items.add(new EmergencyContact(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4)));
        }
        c.close();
        return items;
    }

    public long insertAlert(AlertItem alert) {
        ContentValues cv = new ContentValues();
        cv.put("title", alert.title);
        cv.put("message", alert.message);
        cv.put("when_millis", alert.whenMillis);
        cv.put("type", alert.type);
        return getWritableDatabase().insert("alerts", null, cv);
    }

    public boolean alertExists(String title, String message, long whenMillis, String type) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT 1 FROM alerts WHERE title=? AND message=? AND when_millis=? AND type=? LIMIT 1",
                new String[]{title, message, String.valueOf(whenMillis), type}
        );
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public int deleteAllAlerts() {
        return getWritableDatabase().delete("alerts", null, null);
    }

    public List<AlertItem> getAllAlerts() {
        List<AlertItem> items = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id,title,message,when_millis,type FROM alerts ORDER BY when_millis DESC",
                null
        );
        while (c.moveToNext()) {
            items.add(new AlertItem(c.getInt(0), c.getString(1), c.getString(2), c.getLong(3), c.getString(4)));
        }
        c.close();
        return items;
    }
}
