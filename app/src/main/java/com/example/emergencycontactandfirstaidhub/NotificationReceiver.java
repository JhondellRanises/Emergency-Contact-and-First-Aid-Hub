package com.example.emergencycontactandfirstaidhub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", (int) System.currentTimeMillis());
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        String type = intent.getStringExtra("type");
        NotificationUtils.sendNow(context, title, message, type == null ? "Alert" : type);
    }
}
