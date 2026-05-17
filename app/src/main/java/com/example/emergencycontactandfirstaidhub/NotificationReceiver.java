package com.example.emergencycontactandfirstaidhub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        String type = intent.getStringExtra("type");
        String safeType = type == null ? "Alert" : type;
        NotificationUtils.sendNow(context, title, message, safeType);
    }
}
