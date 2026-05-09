package com.example.emergencycontactandfirstaidhub;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        NotificationUtils.createChannel(this);
        seedDefaultsIfNeeded();

        Button contactsBtn = findViewById(R.id.btnContacts);
        Button firstAidBtn = findViewById(R.id.btnFirstAid);
        Button alertsBtn = findViewById(R.id.btnAlerts);
        Button sosBtn = findViewById(R.id.btnSos);
        TextView disclaimer = findViewById(R.id.tvDisclaimer);

        contactsBtn.setOnClickListener(v -> startActivity(new Intent(this, EmergencyContactsActivity.class)));
        firstAidBtn.setOnClickListener(v -> startActivity(new Intent(this, FirstAidActivity.class)));
        alertsBtn.setOnClickListener(v -> startActivity(new Intent(this, AlertsActivity.class)));
        sosBtn.setOnClickListener(v -> startActivity(new Intent(this, EmergencyContactsActivity.class).putExtra("open_sos", true)));
        disclaimer.setOnClickListener(v -> showDisclaimer());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission();
        }
    }

    private void seedDefaultsIfNeeded() {
        AppDatabase db = new AppDatabase(this);
        if (db.getAllContacts("").isEmpty()) {
            db.insertContact(new EmergencyContact("Police", "911", "National Emergency"));
            db.insertContact(new EmergencyContact("Fire Department", "911", "National Emergency"));
            db.insertContact(new EmergencyContact("Ambulance", "911", "National Emergency"));
            db.insertContact(new EmergencyContact("Disaster Response", "911", "DRRM"));
            db.insertContact(new EmergencyContact("Barangay Office", "09990000000", "Local Authority"));
        }
    }

    private void showDisclaimer() {
        new AlertDialog.Builder(this)
                .setTitle("Disclaimer")
                .setMessage("This app provides basic first aid guidance for emergencies. It is not a substitute for professional medical care. Call emergency services immediately in life-threatening situations.")
                .setPositiveButton("OK", null)
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestNotificationPermission() {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 1001);
        }
    }
}
