package com.example.emergencycontactandfirstaidhub;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationUtils {
    public static final String CHANNEL_ID = "safety_alerts";

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Safety Alerts", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Emergency reminders and urgent alerts");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public static void scheduleNotification(Context context, int id, String title, String message, long whenMillis, String type) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putExtra("type", type);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, whenMillis, pi);
        }
    }

    public static void sendNow(Context context, String title, String message, String type) {
        createChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(type + ": " + title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
    }
}
