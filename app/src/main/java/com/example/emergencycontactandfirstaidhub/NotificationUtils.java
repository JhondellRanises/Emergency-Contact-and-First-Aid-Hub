package com.example.emergencycontactandfirstaidhub;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationUtils {
    private static final String TAG = "NotificationUtils";
    public static final String CHANNEL_REMINDER_ID = "safety_alerts_reminder_v2";
    public static final String CHANNEL_EMERGENCY_ID = "safety_alerts_emergency_v3";

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);

            NotificationChannel reminder = new NotificationChannel(CHANNEL_REMINDER_ID, "Safety Reminders", NotificationManager.IMPORTANCE_HIGH);
            reminder.setDescription("Regular reminder and community alerts");
            manager.createNotificationChannel(reminder);

            NotificationChannel emergency = new NotificationChannel(CHANNEL_EMERGENCY_ID, "Emergency Alerts", NotificationManager.IMPORTANCE_HIGH);
            emergency.setDescription("Urgent safety alerts with alarm sound");
            AudioAttributes attrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            emergency.setSound(getEmergencyAlarmUri(context), attrs);
            emergency.enableVibration(true);
            manager.createNotificationChannel(emergency);
        }
    }

    private static Uri getEmergencyAlarmUri(Context context) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.emergency_alarm);
    }

    public static void scheduleNotification(Context context, int id, String title, String message, long whenMillis, String type) {
        Intent notifIntent = new Intent(context, NotificationReceiver.class);
        notifIntent.putExtra("id", id);
        notifIntent.putExtra("title", title);
        notifIntent.putExtra("message", message);
        notifIntent.putExtra("type", type);
        PendingIntent notifPi = PendingIntent.getBroadcast(
                context,
                id * 2,
                notifIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent popupIntent = buildPopupIntent(context, title, message, type);
        PendingIntent popupPi = PendingIntent.getActivity(
                context,
                id * 2 + 1,
                popupIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        PendingIntent showPi = PendingIntent.getActivity(
                context,
                id * 2 + 99,
                popupIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            // Use alarm-clock path for popup for stronger reliability when app is closed.
            am.setAlarmClock(new AlarmManager.AlarmClockInfo(whenMillis, showPi), popupPi);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, whenMillis, notifPi);
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, whenMillis, popupPi);
            } else {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, whenMillis, notifPi);
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, whenMillis, popupPi);
            }
        }
    }

    public static void sendNow(Context context, String title, String message, String type) {
        createChannel(context);

        Intent fullIntent = buildPopupIntent(context, title, message, type);
        PendingIntent fullPi = PendingIntent.getActivity(
                context,
                (int) System.currentTimeMillis(),
                fullIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_EMERGENCY_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(type + ": " + title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setFullScreenIntent(fullPi, true)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
        launchPopupImmediately(context, title, message, type);
    }

    public static void sendNotificationOnly(Context context, String title, String message, String type) {
        createChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_EMERGENCY_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(type + ": " + title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);
        NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
    }

    private static Intent buildPopupIntent(Context context, String title, String message, String type) {
        Intent intent = new Intent(context, AlertFullscreenActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putExtra("type", type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    private static void launchPopupImmediately(Context context, String title, String message, String type) {
        try {
            context.startActivity(buildPopupIntent(context, title, message, type));
        } catch (Exception ex) {
            Log.w(TAG, "Immediate popup launch blocked by system; notification full-screen intent remains active.", ex);
        }
    }
}
