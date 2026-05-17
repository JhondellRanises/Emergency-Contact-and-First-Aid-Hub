package com.example.emergencycontactandfirstaidhub;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlertFullscreenActivity extends AppCompatActivity {
    private MediaPlayer player;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        acquireWakeLockTemporarily();
        keepScreenActiveForEmergencyAlert();
        setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_alert_fullscreen);

        String title = getIntent().getStringExtra("title");
        String message = getIntent().getStringExtra("message");
        String type = getIntent().getStringExtra("type");
        if (title == null || title.trim().isEmpty()) title = "Safety Alert";
        if (message == null || message.trim().isEmpty()) message = "Please check safety instructions immediately.";
        if (type == null || type.trim().isEmpty()) type = "Reminder";

        TextView tvType = findViewById(R.id.tvAlertType);
        TextView tvWhen = findViewById(R.id.tvAlertWhen);
        TextView tvTitle = findViewById(R.id.tvAlertTitle);
        TextView tvMessage = findViewById(R.id.tvAlertMessage);
        Button btnDismiss = findViewById(R.id.btnDismissAlert);

        tvType.setText("Emergency alert: " + type);
        String whenText = new SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(new Date());
        tvWhen.setText(whenText);
        tvTitle.setText(title);
        tvMessage.setText(message);

        btnDismiss.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        // Keep behavior consistent with emergency message dialogs: require OK tap.
    }

    @Override
    protected void onStart() {
        super.onStart();
        startAlarmSound();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAlarmSound();
    }

    @Override
    protected void onDestroy() {
        releaseWakeLockIfHeld();
        super.onDestroy();
    }

    private void keepScreenActiveForEmergencyAlert() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            );
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void startAlarmSound() {
        if (player != null) return;
        player = MediaPlayer.create(this, R.raw.emergency_alarm);
        if (player != null) {
            player.setLooping(true);
            player.start();
        }
    }

    private void stopAlarmSound() {
        if (player != null) {
            if (player.isPlaying()) player.stop();
            player.release();
            player = null;
        }
    }

    private void acquireWakeLockTemporarily() {
        try {
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (pm == null) return;
            wakeLock = pm.newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                            | PowerManager.ACQUIRE_CAUSES_WAKEUP
                            | PowerManager.ON_AFTER_RELEASE,
                    "EmergencyHotlinePH:AlertWakeLock"
            );
            wakeLock.acquire(2 * 60 * 1000L);
        } catch (Exception ignored) {
            // If wakelock fails on a specific device, keep default screen-on flags behavior.
        }
    }

    private void releaseWakeLockIfHeld() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        wakeLock = null;
    }
}
