package com.example.emergencycontactandfirstaidhub;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.Map;

public class FirstAidActivity extends AppCompatActivity {
    private final Map<String, String> guides = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);
        setTitle("First Aid Guide");
        seedGuides();
        ImageButton btnMore = findViewById(R.id.btnMore);
        btnMore.setOnClickListener(v -> ThemeUtils.showThemeMenu(this, btnMore));

        ListView list = findViewById(R.id.lvFirstAid);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_first_aid_topic, guides.keySet().toArray(new String[0]));
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            String key = adapter.getItem(position);
            new AlertDialog.Builder(this)
                    .setTitle(key)
                    .setMessage(guides.get(key))
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    private void seedGuides() {
        guides.put("Burns", "1. Cool burn under running water for 20 minutes.\n2. Remove tight items near burn.\n3. Cover with sterile non-stick dressing.\nWarning: Call emergency services immediately for severe burns.");
        guides.put("Bleeding", "1. Apply firm pressure with clean cloth.\n2. Elevate injured area if possible.\n3. Do not remove soaked cloth, add more layers.\nWarning: Call emergency services immediately if bleeding is heavy.");
        guides.put("Choking", "1. Encourage coughing if conscious.\n2. Perform 5 back blows and abdominal thrusts.\n3. Start CPR if unresponsive.\nWarning: Call emergency services immediately.");
        guides.put("Fractures", "1. Keep person still and support injured part.\n2. Immobilize with splint.\n3. Apply cold pack wrapped in cloth.\nWarning: Call emergency services immediately.");
        guides.put("CPR", "1. Check response and breathing.\n2. Begin chest compressions at 100-120/min.\n3. Give rescue breaths if trained.\nWarning: Call emergency services immediately.");
        guides.put("Fainting", "1. Lay person flat and raise legs.\n2. Loosen tight clothing.\n3. Monitor breathing.\nWarning: Call emergency services if consciousness does not return quickly.");
        guides.put("Electric shock", "1. Turn off power source first.\n2. Do not touch victim until safe.\n3. Check breathing and start CPR if needed.\nWarning: Call emergency services immediately.");
        guides.put("Heat stroke", "1. Move to cool place.\n2. Cool body with wet cloth/ice packs.\n3. Give sips of cool water if conscious.\nWarning: Call emergency services immediately.");
        guides.put("Poisoning", "1. Remove from poison source.\n2. Do not force vomiting.\n3. Keep poison container for identification.\nWarning: Call emergency services immediately.");
    }
}
