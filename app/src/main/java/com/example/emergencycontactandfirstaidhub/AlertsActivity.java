package com.example.emergencycontactandfirstaidhub;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AlertsActivity extends AppCompatActivity {
    private final Calendar selected = Calendar.getInstance();
    private final SimpleDateFormat fmt = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
    private AppDatabase db;
    private ArrayAdapter<String> listAdapter;
    private List<AlertItem> current = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);
        setTitle("Safety Alerts");
        db = new AppDatabase(this);

        EditText etTitle = findViewById(R.id.etAlertTitle);
        EditText etMessage = findViewById(R.id.etAlertMessage);
        EditText etDate = findViewById(R.id.etAlertDate);
        Spinner spType = findViewById(R.id.spAlertType);
        Button btnSave = findViewById(R.id.btnSaveAlert);
        Button btnSendNow = findViewById(R.id.btnSendNow);
        Button btnDeleteAll = findViewById(R.id.btnDeleteAllAlerts);
        ImageButton btnMore = findViewById(R.id.btnMore);
        ListView lv = findViewById(R.id.lvAlerts);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Reminder", "Urgent", "Community"});
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(typeAdapter);

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        lv.setAdapter(listAdapter);
        refreshList();

        etDate.setText(fmt.format(selected.getTime()));
        etDate.setOnClickListener(v -> pickDateTime(etDate));

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String message = etMessage.getText().toString().trim();
            String type = spType.getSelectedItem().toString();
            if (title.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Title and message are required.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selected.getTimeInMillis() <= System.currentTimeMillis()) {
                Toast.makeText(this, "Please select a future time for scheduled alerts.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!ensureFullscreenPopupPermissionIfNeeded()) {
                return;
            }
            if (!ensureExactAlarmPermissionIfNeeded()) {
                return;
            }
            if (db.alertExists(title, message, selected.getTimeInMillis(), type)) {
                Toast.makeText(this, "Duplicate alert already exists.", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertItem item = new AlertItem(title, message, selected.getTimeInMillis(), type);
            long id = db.insertAlert(item);
            NotificationUtils.scheduleNotification(this, (int) id, title, message, selected.getTimeInMillis(), type);
            Toast.makeText(this, "Alert scheduled.", Toast.LENGTH_SHORT).show();
            refreshList();
        });

        btnSendNow.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String message = etMessage.getText().toString().trim();
            String type = "Urgent";
            if (title.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Title and message are required.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!ensureFullscreenPopupPermissionIfNeeded()) {
                return;
            }
            NotificationUtils.sendNow(this, title, message, type);
            Toast.makeText(this, "Alert sent.", Toast.LENGTH_SHORT).show();
        });
        btnDeleteAll.setOnClickListener(v -> new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete all alerts?")
                .setMessage("This will remove all saved alerts.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteAllAlerts();
                    refreshList();
                    Toast.makeText(this, "All alerts deleted.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show());
        btnMore.setOnClickListener(v -> ThemeUtils.showThemeMenu(this, btnMore));
    }

    private void refreshList() {
        current = db.getAllAlerts();
        List<String> rows = new ArrayList<>();
        for (AlertItem i : current) {
            rows.add(i.type + " | " + i.title + "\n" + fmt.format(i.whenMillis));
        }
        listAdapter.clear();
        listAdapter.addAll(rows);
    }

    private void pickDateTime(EditText target) {
        Calendar now = Calendar.getInstance();
        new DatePickerDialog(this, (v, y, m, d) -> {
            selected.set(Calendar.YEAR, y);
            selected.set(Calendar.MONTH, m);
            selected.set(Calendar.DAY_OF_MONTH, d);
            new TimePickerDialog(this, (tv, h, min) -> {
                selected.set(Calendar.HOUR_OF_DAY, h);
                selected.set(Calendar.MINUTE, min);
                selected.set(Calendar.SECOND, 0);
                target.setText(fmt.format(selected.getTime()));
            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false).show();
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean ensureExactAlarmPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return true;
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (am != null && am.canScheduleExactAlarms()) return true;

        Toast.makeText(this, "Allow exact alarms for on-time scheduled alerts.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:" + getPackageName()));
        startActivity(intent);
        return false;
    }

    private boolean ensureFullscreenPopupPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) return true;
        NotificationManager nm = getSystemService(NotificationManager.class);
        if (nm != null && nm.canUseFullScreenIntent()) return true;

        Toast.makeText(this, "Allow full-screen alerts so popup appears even outside the app.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT, Uri.parse("package:" + getPackageName()));
        startActivity(intent);
        return false;
    }
}
