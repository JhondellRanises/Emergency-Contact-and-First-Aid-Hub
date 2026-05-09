package com.example.emergencycontactandfirstaidhub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            AppDatabase db = new AppDatabase(context);
            long now = System.currentTimeMillis();
            for (AlertItem item : db.getAllAlerts()) {
                if (item.whenMillis > now) {
                    NotificationUtils.scheduleNotification(context, item.id, item.title, item.message, item.whenMillis, item.type);
                }
            }
        }
    }
}
