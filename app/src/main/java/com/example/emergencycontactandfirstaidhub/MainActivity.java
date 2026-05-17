package com.example.emergencycontactandfirstaidhub;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        NotificationUtils.createChannel(this);
        seedDefaultsIfNeeded();

        Button contactsBtn = findViewById(R.id.btnContacts);
        Button firstAidBtn = findViewById(R.id.btnFirstAid);
        Button alertsBtn = findViewById(R.id.btnAlerts);
        Button sosBtn = findViewById(R.id.btnSos);
        ImageButton btnMore = findViewById(R.id.btnMore);
        TextView disclaimer = findViewById(R.id.tvDisclaimer);

        contactsBtn.setOnClickListener(v -> startActivity(new Intent(this, EmergencyContactsActivity.class)));
        firstAidBtn.setOnClickListener(v -> startActivity(new Intent(this, FirstAidActivity.class)));
        alertsBtn.setOnClickListener(v -> startActivity(new Intent(this, AlertsActivity.class)));
        sosBtn.setOnClickListener(v -> startActivity(new Intent(this, EmergencyContactsActivity.class).putExtra("open_sos", true)));
        btnMore.setOnClickListener(v -> showDashboardMenuDialog());
        disclaimer.setOnClickListener(v -> showDisclaimer());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission();
        }
    }

    private void seedDefaultsIfNeeded() {
        AppDatabase db = new AppDatabase(this);
        List<EmergencyContact> existing = db.getAllContacts("");
        boolean hasLegacySeed = existing.size() <= 6 && containsContact(existing, "Police")
                && containsContact(existing, "Fire Department")
                && containsContact(existing, "Ambulance");

        if (existing.isEmpty() || hasLegacySeed) {
            if (hasLegacySeed) {
                for (EmergencyContact contact : existing) {
                    db.deleteContact(contact.id);
                }
            }
            seedGensanHotlines(db);
        }
    }

    private boolean containsContact(List<EmergencyContact> contacts, String name) {
        for (EmergencyContact contact : contacts) {
            if (contact.name.equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    private void seedGensanHotlines(AppDatabase db) {
        // City and emergency offices
        db.insertContact(new EmergencyContact("Task Force Gensan", "887-6018 / 0905-144-3676", "Emergency Office", 1));
        db.insertContact(new EmergencyContact("GSC Police Office", "552-5573 / 0998-598-7207", "Police", 1));
        db.insertContact(new EmergencyContact("City DRRMO", "552-3939 / 552-8861 / 0943-461-4548", "Disaster Response", 1));
        db.insertContact(new EmergencyContact("Bureau of Fire Protection", "552-1160 / 0943-341-5561 / 160", "Fire", 1));
        db.insertContact(new EmergencyContact("Traffic Enforcement Unit", "825-3894 / 0910-043-6910", "Traffic"));
        db.insertContact(new EmergencyContact("Mobile Patrol Unit", "822-0215", "Police"));

        // Police stations
        db.insertContact(new EmergencyContact("Police Station 1", "0998-598-7208", "Police Station"));
        db.insertContact(new EmergencyContact("Police Station 2", "0918-921-3580", "Police Station"));
        db.insertContact(new EmergencyContact("Police Station 3", "0998-598-7212", "Police Station"));
        db.insertContact(new EmergencyContact("Police Station 4", "0998-598-7214", "Police Station"));
        db.insertContact(new EmergencyContact("Police Station 5", "0907-313-4517", "Police Station"));
        db.insertContact(new EmergencyContact("Police Station 6", "0998-598-7218", "Police Station"));
        db.insertContact(new EmergencyContact("Police Station 7", "0998-598-7220", "Police Station"));
        db.insertContact(new EmergencyContact("Police Station 8", "0998-598-7223", "Police Station"));
        db.insertContact(new EmergencyContact("Police Station 9", "0948-874-1661", "Police Station"));
        db.insertContact(new EmergencyContact("Police Station 10", "0999-548-9244", "Police Station"));

        // Hospitals
        db.insertContact(new EmergencyContact("Mindanao Medical Center", "553-8207 / 554-9640", "Hospital"));
        db.insertContact(new EmergencyContact("St. Elizabeth Hospital", "552-3162 / 0919-071-9004", "Hospital"));
        db.insertContact(new EmergencyContact("Gensan Doctors Hospital", "250-2777 / 0933-821-7257", "Hospital"));
        db.insertContact(new EmergencyContact("Socsargen County Hospital", "553-8906 / 0932-692-4708", "Hospital"));
        db.insertContact(new EmergencyContact("Dr. Jorge P. Royeca City Hospital", "552-2811 / 0912-376-2331", "Hospital"));
        db.insertContact(new EmergencyContact("Dadiangas Cooperative Hospital", "552-3942 / 0923-809-4705", "Hospital"));
        db.insertContact(new EmergencyContact("Aguis Clinic", "552-4911", "Clinic"));
        db.insertContact(new EmergencyContact("Gensan Medical Center", "887-9898", "Hospital"));
        db.insertContact(new EmergencyContact("Labella Hospital", "553-3509", "Hospital"));
        db.insertContact(new EmergencyContact("Sarangani Bay Specialists Medical Center", "887-8888 / 0919-067-8395", "Hospital"));
    }

    private void showDisclaimer() {
        new AlertDialog.Builder(this)
                .setTitle("Disclaimer")
                .setMessage("This app provides basic first aid guidance for emergencies. It is not a substitute for professional medical care. Call emergency services immediately in life-threatening situations.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showDashboardMenuDialog() {
        String themeLabel = ThemeUtils.isNightMode(this) ? "Switch to Light Mode" : "Switch to Dark Mode";
        String[] options = {themeLabel, "Close"};

        new AlertDialog.Builder(this)
                .setTitle("Dashboard Menu")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        ThemeUtils.toggleTheme(this);
                        recreate();
                    } else {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestNotificationPermission() {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 1001);
        }
    }
}
